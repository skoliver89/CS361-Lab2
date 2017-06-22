package Labs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class Lab2 {

	public static void main (String[] args)
	{
		Lab2 run = new Lab2();
		int[] p = new int[] {30, 4, 8, 5, 10, 25, 15};
		
		//------------------- Steps 1-4 -------------------//
		Object[] matricesMCM = run.dpMCM(p);
		int[][] m = (int[][]) matricesMCM[0];
		int[][] s = (int[][]) matricesMCM[1];
		System.out.println("---------- DP MCM MATRIX M PRINTOUT ----------");
		run.printMatrix(m);
		System.out.println("---------- DP MCM MATRIX S PRINTOUT ----------");
		run.printMatrix(s);
		
		System.out.println("---------- MEMOIZATION MCM MATRIX M PRINTOUT ----------");
		m = (int[][]) run.memoizedMCM(p)[1];
		run.printMatrix(m);
		
		//------------------- Steps 5-7 -------------------//
		int n = 1000;
		long sTime;
		long fTime;
		for (; n <= 10000000; n *= 10)
		{
			sTime = System.nanoTime();
			int [] data = run.getData(".//lab1_data.txt", n);
			int [] top = run.topTen(data);
			System.out.println("---------- Largest Ten Integers of Array[" + n + "] ---------");
			run.printArray(top);
			data = run.getData(".//lab1_data.txt", n);
			int[] testTop = run.testSortedTopTen(data, n);
			System.out.println("---------- Actual Largest Ten Integers of Array[" + n + "] ---------");
			run.printArray(testTop);
			fTime = System.nanoTime();
			System.out.println("Time to Execute (mS): " + (fTime - sTime)/1000000);
			System.out.println("-----------------------------------------------------------------");
		}
		
		
	}	
		
	
	/*
	 * DP Version of the MCM (Matrix Chain Multiplication) Algorithm (text page 375)
	 * uses -1 in place of infinity symbol
	 */
	public Object[] dpMCM (int [] p)
	{
		int n = p.length - 1;
		int[][] m = new int[n+1][n+1];
		int[][] s = new int[n+1][n+1];
		
		for (int i = 1; i <= n; i++)
		{
			m[i][i] = 0;
		}
		for (int l = 2; l <= n; l++)
		{
			for (int i = 1; i <= (n - l + 1); i++)
			{
				int j = i + l - 1;
				m[i][j] = -1;
				for (int k = i; k <= (j - 1); k++)
				{
					int q = m[i][k] + m[k+1][j] + p[i-1] * p[k] * p[j];
					if (m[i][j] == -1)
					{
						m[i][j] = q;
						s[i][j] = k;
					}
					else if (q < m[i][j])
					{
						m[i][j] = q;
						s[i][j] = k;
					}
				}
			}
		}
		return new Object[] {m,s};
	}
	
	/*
	 * Memoization version of MCM algorithm (text page 388)
	 * Uses -1 in place of infinity
	 */
	public Object[] memoizedMCM(int[] p)
	{
		int n = p.length - 1;
		int[][] m = new int[n+1][n+1];
		for (int i = 1; i <= n; i++)
		{
			for (int j = i; j <= n; j++)
			{
				m[i][j] = -1;
			}
		}
		return lookup(m,p,1,n);
	}
	private Object[] lookup(int[][] m, int[] p, int i, int j)
	{
		if (m[i][j] > -1)
		{
			return new Object[] {m[i][j], m};
		}
		else if (i == j)
		{
			m[i][j] = 0;
		}
		else
		{
			for (int k = i; k <= (j - 1); k++)
			{
				int x = (int) lookup(m, p, i, k)[0];
				int y = (int) lookup(m, p, k+1, j)[0];
				int q = x + y + p[i-1] * p[k] * p[j];
				if (m[i][j] == -1)
				{
					m[i][j] = q;
				}
				else if (q < m[i][j])
				{
					m[i][j] = q;
				}
			}
		}
		return new Object [] {m[i][j], m};
	}
	
	/*
	 * Print out a visual representation of the given matrix.
	 * currently supports only int[][] type matrices.
	 */
	public void printMatrix(int[][] matrix)
	{
		for (int [] row : matrix)
		{
			for (int num : row)
			{
				if (num >= 0)
				{
				System.out.print(String.format("%1$6s", num) + " | ");
				}
				else
				{
					System.out.print(String.format("%1$6s", "INF") + " | ");
				}
			}
			System.out.println();
		}
	}
	
	/*
	 * Determine the largest 10 integers from a given array of integers (recursive)
	 * without sorting
	 * takes an array of integers
	 * return an array of the ten largest ints
	 * Approach: Greedy
	 */
	public int[] topTen(int[] data)
	{
		int[] top = new int[10];
		
		return topTenHelper(data, top, 0);
	}
	private int[] topTenHelper(int[] data, int [] top, int i)
	{
		if (i == 10)
		{
			return top;
		}
		int k = max(data);
		top[i] = data[k];
		data[k] = 0;
		return topTenHelper(data, top, i+1);
	}
	
	public int max (int[] a)
	{
		int max = 0;
		int k = 0;
		int i = 0;
		
		for(int num : a)
		{
			if (num > max)
			{
				max = num;
				k = i;
			}
			i++;
		}
		
		return k;
	}
	
	public void printArray(int[] array)
	{
		int i = 1;
		for (int num : array)
		{
			System.out.println(i + ": " + num);
			i++;
		}
	}
	
	public int[] testSortedTopTen(int[] data, int n)
	{
		int [] testTop = new int[10];
		auxMergeSort(data, 0, n);
		reverseArray(data, n);
		
		for (int i = 0; i < 10; i++)
		{
			testTop[i] = data[i];
		}
		return testTop;
	}
	
	public void reverseArray(int[] array, int n)
	{
		for(int x = 0; x < n/2; x++)
		{
			int a = x;
			int z = n - 1 - x;
			exchange(array,a, z);
		}
	}
	
	// ----------------- Stuff Brought in from Lab 1 ------------------ //
	
	/*
	 * Version 2 of the data reader method from Lab1
	 * No longer reads only a single path name to a Global Array
	 * Takes a path string pointing to a file of integers and the number of integers (n) to read
	 * returns an array of those integers
	 * For the purpose of this lab, n is limited to 1000, 10000, 100000, 1000000, and 10000000
	 */
	public int[] getData (String path, int n)
	{
		int[] data = new int[n];
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = null;
			int i = 0;
			while ((line = reader.readLine()) != null && i < n)
			{
				data[i] = Integer.parseInt(line);
				i++;
			}
			reader.close();
			System.out.println("Data (" + n + ") read into the array!");
		}
		catch(IOException e)
		{
			System.out.println("Cannot find file: " + path);
		}
		return data;
	}
	
	/*
	 * Sort the elements between the startIndex and endIndex using Merge Sort.
	 * Code altered from: http://www.sanfoundry.com/java-program-implement-merge-sort/
	 * Reversed the array of the original sort so that now in decending order.
	 * @param array The given array to be sorted
	 * @param startIndex Index to start sorting at
	 * @param endIndex Index to end sorting at
	 */
	public void auxMergeSort (int[] array, int startIndex, int endIndex)
	{
		int n = endIndex - startIndex;
		if (n <= 1)
		{
			return;
		}
		int midIndex = startIndex + n/2;
		auxMergeSort (array, startIndex, midIndex);
		auxMergeSort (array, midIndex, endIndex);
		merge(array, n, startIndex, midIndex, endIndex);
	}
	private void merge (int[] array, int n, int startIndex, int midIndex, int endIndex)
	{
		int[] temp = new int[n];
		int i = startIndex;
		int j = midIndex;
		for (int k = 0; k < n; k++)
		{
			if (i == midIndex)
			{
				temp[k] = array[j++];
			}
			else if (j == endIndex)
			{
				temp[k] = array[i++];
			}
			else if (array[j] < array[i])
			{
				temp[k] = array[j++];
			}
			else
			{
				temp[k] = array[i++];
			}
		}
		for (int k = 0; k < n; k++)
		{
			array[startIndex + k] = temp[k];
		}
	}
	public void exchange(int[] array, int i, int j)
	{
		int temp = array[j];
		array[j] = array[i];
		array[i] = temp;	
	}
}