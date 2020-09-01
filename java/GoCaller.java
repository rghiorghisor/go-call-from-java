import java.util.Arrays;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Loads a Go library and facilitates the calls towards that library.
 */
public class GoCaller {

	/**
	 *  The underlying loaded native library.
	 */
	private NativeLibrary nativeLibrary;

	/**
	 * Constructor.
	 */
	private GoCaller() {
		init();
	}

	/**
	 * Loads the native library and takes proper initialization actions. 
	 */
	private void init() {
		String os = System.getProperty("os.name").toLowerCase();

		String libExtension = "so";		
		if (os.contains("windows")) {
			libExtension = "dll";
		} else if (os.contains("mac os")) {
			libExtension = "dylib";			
		}

		String pwd = System.getProperty("user.dir");
		System.out.println(pwd);
		String lib = pwd + "/bin/main." + libExtension;		
		nativeLibrary = (NativeLibrary) Native.load(lib, NativeLibrary.class);
	}
	
	/**
	 * Retrieves the addition result of the two values.
	 * 
	 * @param x First number.
	 * @param y Second number.
	 * @return The two numbers sum.
	 */
	public long Add(long x, long y) {		
		return nativeLibrary.Add(x, y);
	}	
	
	/**
	 * Sorts the given array in place.
	 * 
	 * @param arr The array to be sorted.
	 */
	public void Sort(long[] arr) {
		int size = arr.length;
		if (size < 2) {
			return;
		}
		
		Pointer pointer = new Memory(size * Native.getNativeSize(Long.TYPE));
		pointer.write(0, arr, 0, size);
		
		NativeLibrary.Slice.ByValue slice = new NativeLibrary.Slice.ByValue();
		slice.data = pointer;
        slice.len = size;
        slice.cap = size;
		
        nativeLibrary.Sort(slice);
           
        pointer.read(0, arr, 0, size);      
	}
	

	/**
	 * Main method.
	 * 
	 * @param args Any arguments.
	 */
	public static void main(String[] args) {
		GoCaller caller = new GoCaller();

		System.out.println("[Java] Trying to call Go for Add...");		
		long sum = caller.Add(10, 92);		
		System.out.println("[Java] Got result for Add: " + sum);

		System.out.println("[Java] Trying to call Go for Sort...");
		long[] arr = new long[]{3, 4, 1, 5, 2};
		System.out.println("[Java] Array to Sort: " + Arrays.toString(arr));
		
		caller.Sort(arr);		
		System.out.println("[Java] Got result for Add: " + Arrays.toString(arr));}
}

/**
 * Defines the functionality of a native Go library.
 */
interface NativeLibrary extends Library {
	
	/**
	 * A wrapper over the Go Slice that contains a pointer to the data.
	 */
	@Structure.FieldOrder({"data", "len", "cap"})
	public class Slice extends Structure {
		
        public static class ByValue extends Slice implements Structure.ByValue {}
        
        public Pointer data;
        
        public long len;
        
        public long cap;        
    }

	/**
	 * Retrieves the addition result of the two values.
	 * 
	 * @param x First number.
	 * @param y Second number.
	 * @return The two numbers sum.
	 */
	long Add(long x, long y);

	/**
	 * Sorts the given array in place.
	 * 
	 * @param slice The slice containing the array to be sorted.
	 */
	void Sort(NativeLibrary.Slice.ByValue slice);
}

