package io.github.gaeqs.javayoutubedownloader.decrypt;

import io.github.gaeqs.javayoutubedownloader.exception.DownloadException;
import io.github.gaeqs.javayoutubedownloader.util.HTMLUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This decrypt algorithm uses the video player code from the youtube's web to decrypt the signature code.
 */
public class HTML5SignatureDecrypt implements Decrypt {

	private static Pattern[] MAIN_FUNCTION_PATTERNS = new Pattern[]{
			Pattern.compile("\\b[cs]\\s*&&\\s*[adf]\\.set\\([^,]+\\s*,\\s*encodeURIComponent\\s*\\(\\s*([a-zA-Z0-9$]+)\\("),
			Pattern.compile("\\b[a-zA-Z0-9]+\\s*&&\\s*[a-zA-Z0-9]+\\.set\\([^,]+\\s*,\\s*encodeURIComponent\\s*\\(\\s*([a-zA-Z0-9$]+)\\("),
			Pattern.compile("\\b([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)"),
			Pattern.compile("([a-zA-Z0-9$]+)\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)"),
			Pattern.compile("([\"'])signature\\1\\s*,\\s*([a-zA-Z0-9$]+)\\("),
			Pattern.compile("\\.sig\\|\\|([a-zA-Z0-9$]+)\\("),
			Pattern.compile("yt\\.akamaized\\.net/\\)\\s*\\|\\|\\s*.*?\\s*[cs]\\s*&&\\s*[adf]\\.set\\([^,]+\\s*,\\s*(?:encodeURIComponent\\s*\\()?\\s*()$"),
			Pattern.compile("\\b[cs]\\s*&&\\s*[adf]\\.set\\([^,]+\\s*,\\s*([a-zA-Z0-9$]+)\\("),
			Pattern.compile("\\b[a-zA-Z0-9]+\\s*&&\\s*[a-zA-Z0-9]+\\.set\\([^,]+\\s*,\\s*([a-zA-Z0-9$]+)\\("),
			Pattern.compile("\\bc\\s*&&\\s*a\\.set\\([^,]+\\s*,\\s*\\([^)]*\\)\\s*\\(\\s*([a-zA-Z0-9$]+)\\("),
			Pattern.compile("\\bc\\s*&&\\s*[a-zA-Z0-9]+\\.set\\([^,]+\\s*,\\s*\\([^)]*\\)\\s*\\(\\s*([a-zA-Z0-9$]+)\\(")
	};


	public static ConcurrentMap<String, DecryptScript> playerCache = new ConcurrentHashMap<>();

	private DecryptScript getScript(String jsUrl) throws ScriptException {
		DecryptScript script = playerCache.get(jsUrl);
		if (script != null) return script;

		ScriptEngineManager manager = new ScriptEngineManager();
		// use a js script engine
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		String playerScript = getHtml5PlayerScript(jsUrl);
		String decodeFuncName = getMainDecodeFunctionName(playerScript);
		String decodeScript = extractDecodeFunctions(playerScript, decodeFuncName);

		engine.eval(decodeScript);
		Invocable invocable = (Invocable) engine;
		script = new DecryptScript(decodeFuncName, invocable);
		playerCache.put(jsUrl, script);
		return script;
	}

	private String getHtml5PlayerScript(String jsUrl) {
		try {
			return HTMLUtils.readAll(new URL(jsUrl));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getMainDecodeFunctionName(String playerJS) {
		for (Pattern pattern : MAIN_FUNCTION_PATTERNS) {
			Matcher matcher = pattern.matcher(playerJS);
			if (matcher.find()) {
				return matcher.group(1);
			}
		}
		return null;
	}

	public String extractDecodeFunctions(String playerJS, String functionName) {
		StringBuilder decodeScript = new StringBuilder();
		//May change.
		Pattern decodeFunction = Pattern.compile(String.format("(%s=function\\([a-zA-Z0-9$]+\\)\\{.*?\\})[,;]", Pattern.quote(functionName)),
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
	public String decrypt(String jsUrl, String signature) {


		String decodedSignature;
		try {
			DecryptScript script = getScript(jsUrl);
			decodedSignature = (String) script.getInvocable().invokeFunction(script.getFunctionName(), signature);
		} catch (Exception e) {
			throw new DownloadException("Unable to decrypt signature!", e);
		}
		return decodedSignature;
	}
}
