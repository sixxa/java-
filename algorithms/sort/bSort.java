class bSort
{
    static void bubbleSort(int ar[])
    {
        int len = ar.length; 
        for (int i = 0; i < len-1; i++)
            for (int j = 0; j < len-i-1; j++) 
                if (ar[j] > ar[j+1]) 
                {
                    // swapping ar[j+1] and ar[j] without temporary variable     
                    ar[j+1]=ar[j+1] + ar[j];
                    ar[j]=ar[j+1] - ar[j];
                    ar[j+1]=ar[j+1] - ar[j];

                }
    }

    // Print array function
    static void printArray(int ar[])
    {
        int len = ar.length;
        for (int i = 0; i < len; i++)
            System.out.print(ar[i] + " ");

        System.out.println();
    }

    public static void main(String args[])
    {
        int arr[] = {64, 34, 25, 12, 22, 11, 90};

        System.out.println("unsorted array");

        printArray(arr);

        bubbleSort(arr);

        System.out.println("sorted array");

        printArray(arr); 
    }
}