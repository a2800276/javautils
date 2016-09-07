package util.bytes;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by a2800276 on 2016-09-07.
 */
public class ByteListTest {
	static byte[] EMPTY = new byte[0];
	static byte[] BS = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};

	@Test
	public void testAdd() throws Exception {
		ByteList list = new ByteList();
		assertEquals(list.size(), 0);
		list.add((byte) 0x01);
		assertEquals(list.size(), 1);

		list = new ByteList(1000);
		list.add((byte) 0x01);
		assertEquals(list.size(), 1);
	}

	@Test
	public void testAddOffset() throws Exception {
		ByteList list = new ByteList();
		list.add(BS, 1, 4);
		assertEquals(list.size(), 4);
		byte[] expected = {0x01, 0x02, 0x03, 0x04};
		assertEquals(list.toArray(), expected);
	}

	@Test
	public void testAddArray() throws Exception {
		ByteList list = new ByteList();
		list.add(BS);
		assertEquals(list.size(), BS.length);
	}

	@Test
	public void testAddList() throws Exception {
		ByteList list = new ByteList();
		list.add(BS);
		list.add(list);
		assertEquals(list.size(), BS.length * 2);
		byte[] expected = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
		assertEquals(list.toArray(), expected);
	}


	@Test
	public void testToArray() throws Exception {
		ByteList list = new ByteList();
		list.add(BS);
		assertEquals(list.toArray(), BS);
	}

	@Test
	public void testGet() throws Exception {
		ByteList list = new ByteList();
		list.add(BS);

		for (int i = 0; i != list.size(); ++i) {
			assertEquals(list.get(i), (byte) i);
		}

	}

	@Test(expectedExceptions = IndexOutOfBoundsException.class)
	public void testGetOutOfRange() throws Exception {
		ByteList list = new ByteList();
		list.add(BS);
		list.get(10);
	}

	@Test
	public void testGetLast() throws Exception {
		ByteList list = new ByteList();
		list.add(BS);
		assertEquals(list.getLast(), 0x06);

	}

	@Test
	public void testGetFirst() throws Exception {
		ByteList list = new ByteList();
		list.add(BS);
		assertEquals(list.getFirst(), 0x00);

	}

	@Test
	public void testSet() throws Exception {
		ByteList list = new ByteList();
		list.add(BS);
		assertEquals(list.getFirst(), 0x00);
		list.set(0, (byte) 0x01);
		assertEquals(list.getFirst(), 0x01);
		assertEquals(list.getLast(), 0x06);
		list.set(6, (byte) 0x00);
		assertEquals(list.getLast(), 0x00);
	}

	@Test(expectedExceptions = IndexOutOfBoundsException.class)
	public void testSetOutOfRange() throws Exception {
		ByteList list = new ByteList();
		list.add(BS);
		list.set(10, (byte) 0x00);
	}


	@Test
	public void testIndexOf() throws Exception {
		ByteList list = new ByteList();
		list.add(BS);
		assertEquals(
				list.indexOf((byte) 0x02), 2
		);
		assertEquals(
				list.indexOf((byte) 0xff), -1
		);
	}

	@Test
	public void testLastIndexOf() throws Exception {
		byte[] bs = {0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00};
		ByteList list = new ByteList();
		list.add(bs);
		assertEquals(
				list.lastIndexOf((byte) 0x00), 6
		);
		assertEquals(
				list.lastIndexOf((byte) 0xff), -1
		);
	}

	@Test
	public void testSize() throws Exception {
		byte[] bs = {0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00};
		ByteList list = new ByteList();
		assertEquals(list.size(), 0);
		list.add(bs);
		assertEquals(list.size(), 7);

	}

}