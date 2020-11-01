/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import java.nio.charset.Charset;

/**
 * a server implementation based on netty
 *
 * @author lendle
 */
public class TCPServer {

    private int port = -1;
    private EventLoopGroup bossGroup = null, workerGroup = null;
    private ChannelFuture channelFuture = null;
    private RequestHandler requestHandler=null;

    public TCPServer(int port, RequestHandler requestHandler) {
        this.port = port;
        this.requestHandler=requestHandler;
    }

    public void start() throws Exception {
        bossGroup = new NioEventLoopGroup(); // (1)
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap(); // (2)
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class) // (3)
                .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf in = (ByteBuf) msg;
                                try {
                                    String str = in.toString(Charset.forName("utf-8"));
                                    requestHandler.processRequest(str);
                                } finally {
                                    ReferenceCountUtil.release(msg); // (2)
                                    ctx.close();
                                }
                            }

                        });
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128) // (5)
                .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

        // Bind and start to accept incoming connections.
        channelFuture = b.bind(port).sync(); // (7)

        // Wait until the server socket is closed.
        // In this example, this does not happen, but you can do that to gracefully
        // shut down your server.
    }

    public void shutdown() throws InterruptedException {
        workerGroup.shutdownGracefully().sync();
        bossGroup.shutdownGracefully().sync();
        channelFuture.channel().closeFuture().sync();
    }
    
    public static interface RequestHandler{
        public String processRequest(String request);
    }
}
