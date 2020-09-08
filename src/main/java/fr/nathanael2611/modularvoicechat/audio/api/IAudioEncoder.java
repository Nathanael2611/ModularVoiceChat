package fr.nathanael2611.modularvoicechat.audio.api;

public interface IAudioEncoder extends NoExceptionCloseable {
	
	byte[] encode(byte[] pcm);
	
	byte[] silence();
	
}
