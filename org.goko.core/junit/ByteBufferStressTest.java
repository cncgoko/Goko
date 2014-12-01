import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.GkUtils;
import org.goko.core.common.buffer.ByteCommandBuffer;
import org.goko.core.log.GkLog;

/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

public class ByteBufferStressTest implements Runnable{
	private static final GkLog LOG = GkLog.getLogger(ByteBufferStressTest.class);
	private ByteCommandBuffer buffer;
	private int iteration = 100;
	private String name;

	public static void main(String[] args) {
		final ByteCommandBuffer tmpBuffer = new ByteCommandBuffer((byte)';');
		List<ByteBufferStressTest> threads = new ArrayList<ByteBufferStressTest>();


		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true){
					if(tmpBuffer.hasNext()){
						System.out.println(System.currentTimeMillis()+" = " +GkUtils.toString(tmpBuffer.unstackNextCommand()));
					}
				}
			}
		});
		t.start();
		for(int i = 0; i < 1000; i++){
			ByteBufferStressTest thread = new ByteBufferStressTest("Painter +++++++++++++++++++++++++++++++"+i+";", tmpBuffer);
			threads.add(thread);
			new Thread(thread).start();
		}
		try {
			t.join();
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

	public ByteBufferStressTest(String name, ByteCommandBuffer buf) {
		this.name = name;
		this.buffer = buf;
	}

	@Override
	public void run() {
		while(iteration > 0){
			buffer.addAll(GkUtils.toBytesList(name));
			iteration--;
			try {
				Thread.sleep((long) (Math.random()));
			} catch (InterruptedException e) {
				LOG.error(e);
			}
		}
	}


}
