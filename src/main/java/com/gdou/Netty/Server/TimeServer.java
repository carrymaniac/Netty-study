package com.gdou.Netty.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
    public void bind(int port) throws Exception{
        //创建两个NioEventLoopGroup实例 一个用于服务端接受客户端连接,一个用于SocketChannel的网络读写.
        EventLoopGroup bossGroup = new NioEventLoopGroup();//接受新连接线程，主要负责创建新连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();//负责读取数据的线程，主要用于读取数据以及业务逻辑处理
        try{
            //创建了ServerBootStrap对象,它是Netty用于启动Nio服务端的辅助启动类,目的是降低服务端的开发复杂度
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup) //将线程组传入到ServerBootStrap中
                    .channel(NioServerSocketChannel.class) ////指定NIO的模式,设置创建的Channel为NioServerSocketChannel
                    /**
                     * 对于ChannelOption.SO_BACKLOG的解释：
                     * 服务器端TCP内核维护有两个队列，我们称之为A、B队列。客户端向服务器端connect时，会发送带有SYN标志的包（第一次握手），服务器端
                     * 接收到客户端发送的SYN时，向客户端发送SYN ACK确认（第二次握手），此时TCP内核模块把客户端连接加入到A队列中，然后服务器接收到
                     * 客户端发送的ACK时（第三次握手），TCP内核模块把客户端连接从A队列移动到B队列，连接完成，应用程序的accept会返回。也就是说accept
                     * 从B队列中取出完成了三次握手的连接。
                     * A队列和B队列的长度之和就是backlog。当A、B队列的长度之和大于ChannelOption.SO_BACKLOG时，新的连接将会被TCP内核拒绝。
                     * 所以，如果backlog过小，可能会出现accept速度跟不上，A、B队列满了，导致新的客户端无法连接。要注意的是，backlog对程序支持的
                     * 连接数并无影响，backlog影响的只是还没有被accept取出的连接
                     */
                    .option(ChannelOption.SO_BACKLOG,1024) //配置NioServerSocketChannel的TCP参数--缓冲区,
                    .childHandler(new ChildChannelHandler());//绑定一个IO事件的处理类,用于处理网络IO事件,配置具体的数据处理方式

            ChannelFuture channelFuture = b.bind(port).sync();//调用服务端启动辅助类的bind方法绑定监听端口,并调用sync等待绑定操作完成
            //等待服务器链路关闭后main退出
            channelFuture.channel().closeFuture().sync();

        }finally {
            //优雅退出
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args != null && args.length>0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new TimeServer().bind(port);
    }
}
