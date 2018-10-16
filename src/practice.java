
public class practice {

	
	public static void main(String[] args){
		int[] array = {-1,-1,-1,87,47,998,0,43,-66,65,77,-5};
		
		for(int i=0;i<array.length;i++){
			int highest = array[i];
			for(int j=i;j<array.length;j++){
				if(highest<array[j]){
					int temp = highest;
					highest = array[j];	
					array[j] = temp;
				}
			}		
			array[i] = highest;
		}
		
		for(int j=0;j<array.length;j++){
			System.out.println(array[j]);
		}	
	}
}

