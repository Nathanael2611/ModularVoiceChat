package fr.nathanael2611.modularvoicechat.audio.api;

public interface IAudioDecoder extends NoExceptionCloseable {
	
	byte[] decoder(byte[] opus);
	
}
