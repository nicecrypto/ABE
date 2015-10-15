package utils;

/*
 * author: wenzilong,licong
 */

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import abe.Attribute;
import Jama.Matrix;
import utils.PairingManager;

public class Utils {
	public static char SPACE = ' ';
	private static Pairing pairing = PairingManager.getDefaultPairing();
	/**
	 * format the string. i.e. remove redundant space
	 * @param s
	 * @return
	 */
	public static String format(String s){
		return s.trim().replaceAll("\\s+", SPACE+"");
	}
	
	public static boolean isEmptyString(String s){
		return s == null ? true : s.equals("") ? true : false; 
	}
	
	public static Element[] multiple(int[][] matrix, Element[] y){
		if(matrix == null || y == null)
			return null;
		Element[] res = new Element[matrix.length];
		for(int i=0; i<matrix.length; i++){
			res[i] = multiple(matrix[i], y);
		}
		return res;
	}
	
	private static Element multiple(int[] array, Element[] y){
		if(array == null || y == null || array.length != y.length)
			return null;
		Element res = pairing.getZr().newZeroElement();
		for(int i=0; i<array.length; i++){
			res.add(y[i].duplicate().mul(array[i]));
		}
		return res;
	}
	
	public static <T> void printArray(T[] array){
		System.out.println("-------------array begin-------------");
		for(int i=0; i<array.length; i++){
			System.out.println(array[i]);
		}
		System.out.println("-------------array end-------------");
	}
	
	public static Element innerProduct(Element[] a, Element[] b){
		if(a == null || b == null || a.length == 0 || b.length == 0 || a.length != b.length){
			return null;
		}
		
		Element res = pairing.getZr().newZeroElement();
		for(int i=0; i<a.length; i++){
			res.add(a[i].duplicate().mul(b[i]));
		}
		return res;
	}
	
	//solve xA=b
	// x = b * A^-1
	public static double[] solve(int[][] A, int[] b){
		if(A == null || b == null)
			return null;
		int rows = A.length;
		int cols = A[0].length;
		
		//filter all zero columns
		List<Integer> all0cols = new ArrayList<Integer>();
		boolean flag = true;
		for(int j=0; j<cols; j++){
			for(int i=0; i<rows; i++){
				if(A[i][j] != 0){
					flag = false;
					break;
				}
			}
			if(flag){
				all0cols.add(j);
			}
			flag = true;
		}

		double[][] _A = new double[rows][];
		for(int i=0; i<rows; i++){
			_A[i] = new double[cols - all0cols.size()];
			int k = 0;
			for(int j=0; j<cols; j++){
				if(all0cols.contains(j))
					continue;
				_A[i][k++] = A[i][j];
			}
		}
		Matrix mA = new Matrix(_A);
		if(mA.getColumnDimension()==mA.getRowDimension()){
		mA = mA.inverse();
//		System.out.println("mA:");
//		mA.print(mA.getRowDimension(), 3);
		
		double[] _b = new double[mA.getColumnDimension()];
		for(int i=0; i<_b.length; i++){
			_b[i] = b[i];
		}
		Matrix mb = new Matrix(_b, 1);
//		mb.print(mb.getRowDimension(), 3);
		
		Matrix res = mb.times(mA);
//		System.out.println("solution:");
//		res.print(res.getRowDimension(), 3);
		return res.getRowPackedCopy();
		}
		else {
			
		    return null;
		}
	}
	
	public static void printMatrix(int[][] m){
		
		if(m == null)
			return;
		for(int i=0; i<m.length; i++){
			for(int j=0; j<m[i].length; j++){
				System.out.print(m[i][j] + "\t");
			}
			System.out.println();
		}
	}
	
	public static Map<Integer, Integer> attributesMatching(Attribute[] attributes, Map<Integer, String> rho){
		
	    Map<Integer, Integer> setI= new HashMap<Integer,Integer>();
				
		for (int i = 0; i < attributes.length; i++) {
			for (Map.Entry<Integer, String> entry : rho.entrySet()) {
				if (entry.getValue().equals(attributes[i].toString())) {
					setI.put(entry.getKey(),i);
				}
			}
		}
		
		return setI;
	}
	
	
	public static Element[] computeOmega(int[][] matrix,Map<Integer, Integer> setI){
		
		int cols = matrix[0].length;
		int[][] Mi = new int[setI.size()][cols];
		
		int j=0;
		for(Entry<Integer, Integer> entry : setI.entrySet()){
			   System.arraycopy(matrix[entry.getKey()], 0, Mi[j], 0, cols);
		       j++;
		}
		
//		System.out.println("Mi:");
//		Utils.printMatrix(Mi);
		
		//target vector [1, 0, 0, ... ,0]
		int[] b = new int[Mi.length];
		b[0] = 1;
		for (int i = 1; i < b.length; i++) {
			b[i] = 0;
		}

		double[] solution = Utils.solve(Mi, b);
		if (solution == null) {
			System.out.println("Secret key can not satisfy the policy in the ciphertext!");
			System.out.println("Decryption unsuccessfully!");
			return null;
		}
		
		Element[] omega = new Element[solution.length];
		for (int i = 0; i < omega.length; i++) {
			omega[i] = pairing.getZr().newElement((int) solution[i]);
		}
		
//		Utils.printArray(omega);
		//System.out.println("innerProduct:" + Utils.innerProduct(shares, omega));
		return omega;
	}
	
	
	public static void main(String[] args) {
		double[] d = new double[]{1, 0, 0};
		Matrix m = new Matrix(1, 3);
		m = new Matrix(d, 1);
		m.print(m.getRowDimension(), 3);
	}

}
