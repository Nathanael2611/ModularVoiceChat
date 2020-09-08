package fr.nathanael2611.modularvoicechat.audio.api;

public interface NoExceptionCloseable extends AutoCloseable {
	
	@Override
	void close();
	
}
