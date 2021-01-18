package fr.nathanael2611.modularvoicechat.audio.impl;

import fr.nathanael2611.modularvoicechat.audio.api.IAudioEncoder;
import net.labymod.opus.OpusCodec;

public class OpusEncoder implements IAudioEncoder
{

    private OpusCodec codec;

    public OpusEncoder(int sampleRate, int channel, int milliseconds, int bitrate, int signal, int bufferSize)
    {
        this.codec = OpusCodec.newBuilder()
                .withSampleRate(sampleRate)
                .withChannels(channel)
                .withFrameSize(sampleRate / (1000 / milliseconds))
                .withBitrate(96000).withMaxFrameSize(bufferSize).build();
    }

    @Override
    public byte[] encode(byte[] pcm)
    {
        return this.codec.encodeFrame(pcm);
    }

    @Override
    public byte[] silence()
    {
        return new byte[] { -8, -1, -2 };
    }

    @Override
    public int encoderId()
    {
        return 0;
    }

    @Override
    public void close()
    {
        this.codec.destroy();
    }
}
