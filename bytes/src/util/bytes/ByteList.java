package util.bytes;

/**
 * Created by a2800276 on 2016-09-07.
 */
public class ByteList {
	/**
	 * default initial size of the (internal) array
	 */
	private static final int DEFAULT_SIZE = 1024;
	private static final double RESIZE_FACTOR = 1.8;

	/**
	 * internal storage
	 */

	private byte[] bytes;

	/**
	 * current position in the internal storage
	 */
	private int pos;

	/**
	 * contructs a variable array with the default size
	 */
	public ByteList() {
		this.bytes = new byte[DEFAULT_SIZE];
	}

	/**
	 * initializes an array with the indicated initial size
	 */
	public ByteList(int size) {
		bytes = new byte[size];
	}

	/**
	 * append the passed by to the storage
	 */
	public void add(byte b) {
		ensureCapacity(1);
		bytes[pos++] = b;
	}

	public void add(byte[] bs, int offset, int length) {
		if (offset + length > bs.length || offset < 0 || length < 0)
			throw new ArrayIndexOutOfBoundsException("Not a valid offset or length. arr.length: " + bs.length + " offset: " + offset + " length: " + length);
		if (length == 0)
			return;
		ensureCapacity(length);
		System.arraycopy(bs, offset, this.bytes, pos, length);
		pos += length;
	}


	public void add(ByteList list) {
		add(list.toArray());
	}

	public void add(byte[] arr) {
		add(arr, 0, arr.length);
	}


	/**
	 * create a byte array from the inserted data.
	 */
	public byte[] toArray() {
		byte[] retArr = new byte[pos];
		System.arraycopy(bytes, 0, retArr, 0, pos);
		return retArr;
	}

	public byte get(int index) {
		if (!(index < size()))
			throw new IndexOutOfBoundsException();
		return bytes[index];
	}

	public byte getLast() {
		return get(size() - 1);
	}

	public byte getFirst() {
		return get(0);
	}

	public byte set(int index, byte element) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		byte ret = get(index);
		this.bytes[index] = element;
		return ret;
	}


	public int indexOf(byte value) {
		for (int i = 0; i != this.size(); ++i) {
			if (value == this.bytes[i]) {
				return i;
			}
		}
		return -1;

	}

	public int lastIndexOf(byte value) {
		for (int i = this.size() - 1; i != -1; --i) {
			if (value == this.bytes[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * returns the current size of the content.
	 */
	public int size() {
		return pos;
	}

	/**
	 * makes sure that there is at least room for
	 * <code>size</code> further elements in the internal array
	 * and "grows" it if necessary.
	 */
	private void ensureCapacity(int size) {
		int capacity = bytes.length - pos;
		if (capacity < size)
			resize(bytes.length + size);
	}

	/**
	 * "grows" the array to a new length of <code>len</code>.
	 */
	private void resize(int len) {
		int newLen = max(len, (int) (bytes.length * RESIZE_FACTOR));
		byte[] newArr = new byte[newLen];
		System.arraycopy(bytes, 0, newArr, 0, pos);
		bytes = newArr;
	}

	/**
	 * utility to return the larger of the two provided
	 * parameters.
	 */
	private int max(int i, int j) {
		return i > j ? i : j;
	}

}
