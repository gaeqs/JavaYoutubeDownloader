package io.github.gaeqs.javayoutubedownloader.tag;

import io.github.gaeqs.javayoutubedownloader.JavaYoutubeDownloader;

import java.util.HashMap;

/**
 * This special {@link HashMap} contains all youtube's iTag possibilities and their properties.
 * You can acces to this map using {@link JavaYoutubeDownloader#getITagMap()} or {@link ITagMap#MAP}.
 */
public class ITagMap extends HashMap<Integer, StreamType> {

	public static final ITagMap MAP = new ITagMap();

	private ITagMap() {
		put(5, new StreamType(Container.FLV, Encoding.H263, VideoQuality.p240, Encoding.MP3, AudioQuality.k64, FormatNote.NONE));
		put(6, new StreamType(Container.FLV, Encoding.H263, VideoQuality.p270, Encoding.MP3, AudioQuality.k64, FormatNote.NONE));
		put(13, new StreamType(Container.GP3, Encoding.MP4, VideoQuality.p144, Encoding.AAC, AudioQuality.k24, FormatNote.NONE));
		put(17, new StreamType(Container.GP3, Encoding.MP4, VideoQuality.p144, Encoding.AAC, AudioQuality.k24, FormatNote.NONE));
		put(18, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p360, Encoding.AAC, AudioQuality.k96, FormatNote.NONE));
		put(22, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p720, Encoding.AAC, AudioQuality.k192, FormatNote.NONE));
		put(34, new StreamType(Container.FLV, Encoding.H264, VideoQuality.p360, Encoding.AAC, AudioQuality.k128, FormatNote.NONE));
		put(35, new StreamType(Container.FLV, Encoding.H264, VideoQuality.p480, Encoding.AAC, AudioQuality.k128, FormatNote.NONE));
		put(36, new StreamType(Container.GP3, Encoding.MP4, VideoQuality.p240, Encoding.AAC, AudioQuality.k36, FormatNote.NONE));
		put(37, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p1080, Encoding.AAC, AudioQuality.k192, FormatNote.NONE));
		put(38, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p3072, Encoding.AAC, AudioQuality.k192, FormatNote.NONE));
		put(43, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p360, Encoding.VORBIS, AudioQuality.k128, FormatNote.NONE));
		put(44, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p480, Encoding.VORBIS, AudioQuality.k128, FormatNote.NONE));
		put(45, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p720, Encoding.VORBIS, AudioQuality.k192, FormatNote.NONE));
		put(46, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p1080, Encoding.VORBIS, AudioQuality.k192, FormatNote.NONE));
		put(59, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p480, Encoding.AAC, AudioQuality.k128, FormatNote.NONE));
		put(78, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p480, Encoding.AAC, AudioQuality.k128, FormatNote.NONE));

		//3D videos
		put(82, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p360, Encoding.AAC, AudioQuality.k128, FormatNote.THREE_DIMENSIONAL));
		put(83, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p480, Encoding.AAC, AudioQuality.k128, FormatNote.THREE_DIMENSIONAL));
		put(84, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p720, Encoding.AAC, AudioQuality.k192, FormatNote.THREE_DIMENSIONAL));
		put(85, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p1080, Encoding.AAC, AudioQuality.k192, FormatNote.THREE_DIMENSIONAL));
		put(100, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p360, Encoding.VORBIS, AudioQuality.k128, FormatNote.THREE_DIMENSIONAL));
		put(101, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p480, Encoding.VORBIS, AudioQuality.k192, FormatNote.THREE_DIMENSIONAL));
		put(102, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p720, Encoding.VORBIS, AudioQuality.k192, FormatNote.THREE_DIMENSIONAL));

		//Apple HTTP Live Streaming
		put(91, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p144, Encoding.AAC, AudioQuality.k48, FormatNote.HLS));
		put(92, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p240, Encoding.AAC, AudioQuality.k48, FormatNote.HLS));
		put(93, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p360, Encoding.AAC, AudioQuality.k128, FormatNote.HLS));
		put(94, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p480, Encoding.AAC, AudioQuality.k128, FormatNote.HLS));
		put(95, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p720, Encoding.AAC, AudioQuality.k256, FormatNote.HLS));
		put(96, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p1080, Encoding.AAC, AudioQuality.k256, FormatNote.HLS));
		put(132, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p240, Encoding.AAC, AudioQuality.k48, FormatNote.HLS));
		put(151, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p72, Encoding.AAC, AudioQuality.k24, FormatNote.HLS));

		//Dash mp4 video
		put(133, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p240, FormatNote.DASH));
		put(134, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p360, FormatNote.DASH));
		put(135, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p480, FormatNote.DASH));
		put(136, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p720, FormatNote.DASH));
		put(137, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p1080, FormatNote.DASH));
		put(138, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p2160, FormatNote.DASH));
		put(160, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p144, FormatNote.DASH));
		put(212, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p480, FormatNote.DASH));
		put(264, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p1440, FormatNote.DASH));
		put(298, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p720, FormatNote.DASH, FPS.f60));
		put(299, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p1080, FormatNote.DASH, FPS.f60));
		put(266, new StreamType(Container.MP4, Encoding.H264, VideoQuality.p2160, FormatNote.DASH));

		//Dash mp4 audio
		put(139, new StreamType(Container.M4A, Encoding.AAC, AudioQuality.k48, FormatNote.DASH));
		put(140, new StreamType(Container.M4A, Encoding.AAC, AudioQuality.k128, FormatNote.DASH));
		put(141, new StreamType(Container.M4A, Encoding.AAC, AudioQuality.k256, FormatNote.DASH));
		put(256, new StreamType(Container.M4A, Encoding.AAC, AudioQuality.k24, FormatNote.DASH));
		put(258, new StreamType(Container.M4A, Encoding.AAC, AudioQuality.k24, FormatNote.DASH));
		put(325, new StreamType(Container.M4A, Encoding.DTSE, AudioQuality.k24, FormatNote.DASH));
		put(328, new StreamType(Container.M4A, Encoding.EC_3, AudioQuality.k24, FormatNote.DASH));

		//Dash webm
		put(167, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p360, FormatNote.DASH));
		put(168, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p480, FormatNote.DASH));
		put(169, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p720, FormatNote.DASH));
		put(170, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p1080, FormatNote.DASH));
		put(218, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p480, FormatNote.DASH));
		put(219, new StreamType(Container.WEBM, Encoding.VP8, VideoQuality.p480, FormatNote.DASH));
		put(278, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p144, FormatNote.DASH));
		put(242, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p240, FormatNote.DASH));
		put(243, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p360, FormatNote.DASH));
		put(244, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p480, FormatNote.DASH));
		put(245, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p480, FormatNote.DASH));
		put(246, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p480, FormatNote.DASH));
		put(247, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p720, FormatNote.DASH));
		put(248, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p1080, FormatNote.DASH));
		put(271, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p1440, FormatNote.DASH));
		put(272, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p2160, FormatNote.DASH));
		put(302, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p720, FormatNote.DASH, FPS.f60));
		put(303, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p1080, FormatNote.DASH, FPS.f60));
		put(308, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p1440, FormatNote.DASH, FPS.f60));
		put(313, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p2160, FormatNote.DASH));
		put(315, new StreamType(Container.WEBM, Encoding.VP9, VideoQuality.p2160, FormatNote.DASH, FPS.f60));

		//Dash webm audio
		put(171, new StreamType(Container.WEBM, Encoding.VORBIS, AudioQuality.k128, FormatNote.DASH));
		put(172, new StreamType(Container.WEBM, Encoding.VORBIS, AudioQuality.k256, FormatNote.DASH));

		//Dash webm audio with opus
		put(249, new StreamType(Container.WEBM, Encoding.OPUS, AudioQuality.k50, FormatNote.DASH));
		put(250, new StreamType(Container.WEBM, Encoding.OPUS, AudioQuality.k70, FormatNote.DASH));
		put(251, new StreamType(Container.WEBM, Encoding.OPUS, AudioQuality.k160, FormatNote.DASH));
	}

}
