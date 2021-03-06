package s4.B183372; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID.
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity,
}
*/

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    //IQ保存用
    //double [][] IQ_save ;


    byte [] subBytes(byte [] x, int start, int end) {
	// corresponding to substring of String for  byte[] ,
	// It is not implement in class library because internal structure of byte[] requires copy.
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
	return result;
    }

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
	return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) { myTarget = target;}
    public void setSpace(byte []space) {
	myFrequencer = new Frequencer();
	mySpace = space;
   myFrequencer.setSpace(space);
    }
    /*
    public void calc_IQ(){
        //IQ_save = new double [myTarget.length][myTarget.length+1];
        for (int i=0;i<myTarget.length;i++){
            for (int j=i+1;j<myTarget.length+1;j++){
                myFrequencer.setTarget(subBytes(myTarget, i, j));
                IQ_save[i][j] = iq(myFrequencer.frequency());
            }
        }
    }

    public double estimation(){
	boolean [] partition = new boolean[myTarget.length+1];
	int np;
	np = 1<<(myTarget.length-1);
	// System.out.println("np="+np+" length="+myTarget.length);
	double value = Double.MAX_VALUE; // value = mininimum of each "value1".

    //calculate IQ
        //calc_IQ();


    IQ_save = new double [myTarget.length][myTarget.length+1];

	for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions.
	    // binary representation of p forms partition.
	    // for partition {"ab" "cde" "fg"}
	    // a b c d e f g   : myTarget
	    // T F T F F T F T : partition:
	    partition[0] = true; // I know that this is not needed, but..
	    for(int i=0; i<myTarget.length -1;i++) {
		partition[i+1] = (0 !=((1<<i) & p));
	    }
	    partition[myTarget.length] = true;

	    // Compute Information Quantity for the partition, in "value1"
	    // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
            double value1 = (double) 0.0;
	    int end = 0;;
	    int start = end;
	    while(start<myTarget.length) {
            //System.out.write(myTarget[end]);
            end++;;
            while(partition[end] == false) {
                // System.out.write(myTarget[end]);
                end++;
            }
             //System.out.print("("+start+","+end+")");

            if(IQ_save[start][end]==0.0){
                myFrequencer.setTarget(subBytes(myTarget, start, end));
                IQ_save[start][end] = iq(myFrequencer.frequency());
            }
            value1 = value1 + IQ_save[start][end];
            start = end;
	    }
             //System.out.println(" "+ value1);

            // Get the minimal value in "value"
	    if(value1 < value) value = value1;
	}
	return value;
}*/

public double estimation(){

  if (myTarget == null || myTarget.length == 0) return 0.0;
  if (mySpace == null || mySpace.length == 0) return Double.MAX_VALUE;

  myFrequencer.setTarget(myTarget);

  double[] IQ_save = new double[myTarget.length+1];

  for(int end=1;end<=myTarget.length;end++){
    double value = Double.MAX_VALUE;

    for(int start=end-1; start>=0; start--){
      int freq = myFrequencer.subByteFrequency(start,end);
      if(freq == 0){
        if(start == end-1){
          return Double.MAX_VALUE;
        }
        break;
      }
      double value1 = iq(freq)+IQ_save[start];
      if(value>value1) value  = value1;
    }
    IQ_save[end] = value;
  }

  return IQ_save[myTarget.length];
}

    public static void main(String[] args) {
	InformationEstimator myObject;
	double value;
	myObject = new InformationEstimator();
	myObject.setSpace("3210321001230123".getBytes());
	myObject.setTarget("0".getBytes());
	value = myObject.estimation();
	System.out.println(">0 "+value);
	myObject.setTarget("01".getBytes());
	value = myObject.estimation();
	System.out.println(">01 "+value);
	myObject.setTarget("0123".getBytes());
	value = myObject.estimation();
	System.out.println(">0123 "+value);
	myObject.setTarget("00".getBytes());
	value = myObject.estimation();
	System.out.println(">00 "+value);
    }
}
