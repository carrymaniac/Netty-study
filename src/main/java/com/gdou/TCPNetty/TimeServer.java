package com.gdou.TCPNetty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {
    public void bind(int port) throws Exception{
        //创建两个NioEventLoopGroup实例 一个用于服务端接受客户端连接,一个用于SocketChannel的网络读写.
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //创建了ServerBootStrap对象,它是Netty用于启动Nio服务端的辅助启动类,目的是降低服务端的开发复杂度
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup) //将线程组传入到ServerBootStrap中
                    .channel(NioServerSocketChannel.class) //设置创建的Channel为NioServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG,1024) //配置NioServerSocketChannel的TCP参数,
                    .childHandler(new ChildChannelHandler());//绑定一个IO事件的处理类,用于处理网络IO事件

            ChannelFuture channelFuture = b.bind(port).sync();//调用服务端启动辅助类的bind方法绑定监听端口,并调用sync等待绑定操作完成
            //等待服务器链路关闭后main退出
            channelFuture.channel().closeFuture().sync();

        }finally {
            //优雅退出
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            socketChannel.pipeline().addLast(new StringDecoder());
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }
}
