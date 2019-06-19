package com.degoos.javayoutubedownloader.decrypt;

import com.degoos.javayoutubedownloader.exception.DownloadException;
import com.degoos.javayoutubedownloader.util.HTMLUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This decrypt algorithm used the video player code from the youtube's web to decrypt the signature code.
 */
public class HTML5SignatureDecrypt implements Decrypt {

	public String sig;
	public URI playerURI;
	public static ConcurrentMap<String, String> playerCache = new ConcurrentHashMap<>();

	public HTML5SignatureDecrypt(String signature, URI playerURI) {
		this.sig = signature;
		this.playerURI = playerURI;
	}


	private String getHtml5PlayerScript() {
		String url = playerCache.get(playerURI.toString());

		if (url == null) {
			try {
				String result = HTMLUtils.readAll(playerURI.toURL());
				playerCache.put(playerURI.toString(), result);
				return result;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return url;
	}

	private String getMainDecodeFunctionName(String playerJS) {

		String[] patterns = {"([\\w$]+)\\s*=\\s*function\\(([\\w$]+)\\).\\s*\\2=\\s*\\2\\.split\\(\"\"\\)\\s*;"};

		for (String pattern : patterns) {
			Pattern decodeFunctionName = Pattern.compile(pattern);
			Matcher decodeFunctionNameMatch = decodeFunctionName.matcher(playerJS);
			if (decodeFunctionNameMatch.find()) {
				return decodeFunctionNameMatch.group(1);
			}
		}
		return null;
	}

	public String extractDecodeFunctions(String playerJS, String functionName) {
		StringBuilder decodeScript = new StringBuilder();
		Pattern decodeFunction = Pattern
				// this will probably change from version to version so
				// changes have to be done here
				.compile(String.format("(%s=function\\([a-zA-Z0-9$]+\\)\\{.*?\\})[,;]", Pattern.quote(functionName)),
						Pattern.DOTALL);
		Matcher decodeFunctionMatch = decodeFunction.matcher(playerJS);
		if (decodeFunctionMatch.find()) {
			decodeScript.append(decodeFunctionMatch.group(1)).append(';');
		} else {
			throw new DownloadException("Unable to extract the main decode function!");
		}

		// determine the name of the helper function which is used by the
		// main decode function
		Pattern decodeFunctionHelperName = Pattern.compile("\\);([a-zA-Z0-9]+)\\.");
		Matcher decodeFunctionHelperNameMatch = decodeFunctionHelperName.matcher(decodeScript.toString());
		if (decodeFunctionHelperNameMatch.find()) {
			final String decodeFuncHelperName = decodeFunctionHelperNameMatch.group(1);

			Pattern decodeFunctionHelper = Pattern.compile(
					String.format("(var %s=\\{[a-zA-Z0-9]*:function\\(.*?\\};)", Pattern.quote(decodeFuncHelperName)),
					Pattern.DOTALL);
			Matcher decodeFunctionHelperMatch = decodeFunctionHelper.matcher(playerJS);
			if (decodeFunctionHelperMatch.find()) {
				decodeScript.append(decodeFunctionHelperMatch.group(1));
			} else {
				throw new DownloadException("Unable to extract the helper decode functions!");
			}

		} else {
			throw new DownloadException("Unable to determine the name of the helper decode function!");
		}
		return decodeScript.toString();
	}

	@Override
	public String decrypt() {
		ScriptEngineManager manager = new ScriptEngineManager();
		// use a js script engine
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		final String playerScript = getHtml5PlayerScript();
		final String decodeFuncName = getMainDecodeFunctionName(playerScript);
		final String decodeScript = extractDecodeFunctions(playerScript, decodeFuncName);

		String decodedSignature = null;
		try {
			// evaluate script
			engine.eval(decodeScript);
			Invocable inv = (Invocable) engine;
			// execute the javascript code directly
			decodedSignature = (String) inv.invokeFunction(decodeFuncName, sig);
		} catch (Exception e) {
			throw new DownloadException("Unable to decrypt signature!");
		}
		return decodedSignature;
	}
}
