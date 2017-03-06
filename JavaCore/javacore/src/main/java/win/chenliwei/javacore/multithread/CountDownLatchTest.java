/**
 * @author: ChenLiwei
 * 2017-03-06
 * CountDownLatchTest.java
 * Comments: It is to show the red packet in WeChat game using CountDownLatch synchronization
 * Which means an actions can be performed only at the latch value is 0.
 * Image the red packet game, all the other persons are waiting while you are packeting money
 * then, the red packets sent out, all of them includes you can open it with your share.
 * when all the packets are robbed out, we should set the packet as an empty one and tell the
 * following people not to rob again.
 */
package win.chenliwei.javacore.multithread;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CountDownLatchTest {

	public static void main(String[] args) throws InterruptedException {
		long current;
		Set<String> wechat = new HashSet<>();
		wechat.add("chenliwei");
		wechat.add("zhangpeng");
		wechat.add("yaoxiang");
		wechat.add("zhangwei1");
		wechat.add("zhangwei2");
		RedPacket packet = null;
		CountDownLatch waitRedPacket = new CountDownLatch(1);
		class preparePacket implements Callable<RedPacket>{
			private CountDownLatch cdl;
			private String owner;
			private double amount;
			private int share;
			
			public preparePacket(CountDownLatch cdl, String owner, double amount, int share) {
				this.cdl = cdl;
				this.owner = owner;
				this.amount = amount;
				this.share = share;
			}

			@Override
			public RedPacket call() throws Exception {
				System.out.println(owner + "'s red packet will send out in 3 second, prepare!");
				Thread.sleep(3000);
				RedPacket packet = new RedPacket(cdl, owner, amount, share);
				cdl.countDown();
				return packet;
			}
			
		}
		try {
			packet = new FutureTask<RedPacket>(new preparePacket(waitRedPacket,"chenliwei",100,4)).get();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		waitRedPacket.await();
		current = System.currentTimeMillis();
		CountDownLatch  waitRobOut = new CountDownLatch(packet.getShare());
		class RobPacket implements Runnable{
			private CountDownLatch cdl;
			private RedPacket packet;
			private String name;
			
			public RobPacket(CountDownLatch cdl, RedPacket packet, String name) {
				this.cdl = cdl;
				this.packet = packet;
				this.name = name;
			}

			@Override
			public void run() {
				packet.luckyHand(name);
				try {
					Thread.sleep((long)(3000 * Math.random()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cdl.countDown();
			}
		}
		for(String member : wechat) new Thread(new RobPacket(waitRobOut,packet, member)).start();
		waitRobOut.await();
		System.out.println("All packets are robbed out in " + (int)((System.currentTimeMillis() - current)/1000) + " seconds");
		packet.getDetail();
	}

}


class RedPacket{
	private String owner;
	private double amount;
	private int share;
	private volatile SortedMap<Date,String> detail;
	public RedPacket(CountDownLatch cdl,String owner, double amount, int share) throws InterruptedException {
		this.owner = owner;
		this.amount = amount;
		this.share = share;
		this.detail = new TreeMap<Date,String>();
	}
	
	public String getOwner() {
		return owner;
	}

	public int getShare() {
		return share;
	}

	public void getDetail() {
		System.out.println("Look lucky hand of " + owner + "'s red packet");
		detail.forEach((k,v)->{System.out.println(v);});
	}
	
	public synchronized void luckyHand(String name){
		if(share == 0) return;
		if(share == 1) {
			detail.put(new Date(), name + " got $" + amount);
			share = 0;
			amount = 0;
		}else{
			double hand = amount * Math.random();
			if (hand < 0.01) hand = 0.01;
			detail.put(new Date(), name + " got $" + hand);
			amount -= hand;
			share--;
		}
	}
}