package com.gdou.Netty.Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;


public class TimeServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将msg转换为Netty的ByteBuf对象
        ByteBuf buf = (ByteBuf) msg;
        //读取缓冲区可读的字节数,并且创建byte数组
        byte[] req = new byte[buf.readableBytes()];
        //将缓冲区中的字节数组复制到新建的byte数组中
        buf.readBytes(req);
        //获取信息
        String body = new String(req,"UTF-8");
        System.out.println("The time server receive order:" + body);
        String currentTime  = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
        //拷贝为数组
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        //调用异步方法发送应答消息给客户端
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将消息发送队列中的消息写入到SocketChannek发送给对方
        //为了性能考虑,Netty中的write方法不直接把消息写入给SocketChannel中,调用write只是将待发送的消息放在缓冲数组中,再调用flush方法,
        //将缓冲区中所有的消息全部写给SocketChannel中
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常发生 关闭
        ctx.close();
    }
}
