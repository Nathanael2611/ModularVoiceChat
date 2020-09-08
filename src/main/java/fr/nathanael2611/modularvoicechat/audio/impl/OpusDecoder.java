package fr.nathanael2611.modularvoicechat.audio.impl;

import fr.nathanael2611.modularvoicechat.audio.api.IAudioDecoder;
import net.labymod.opus.OpusCodec;

public class OpusDecoder implements IAudioDecoder
{

    private OpusCodec codec;

    public OpusDecoder(int sampleRate, int channel, int milliseconds, int bufferSize)
    {
        this.codec = OpusCodec.newBuilder()
                .withChannels(channel)
                .build();
    }

    @Override
    public byte[] decoder(byte[] opus)
    {
        return this.codec.decodeFrame(opus);
    }

    @Override
    public void close()
    {
        this.codec.destroy();
    }
}
