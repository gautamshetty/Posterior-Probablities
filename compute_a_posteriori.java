import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * @author gautamshetty
 */
public class compute_a_posteriori {

	public static void main(String[] args) {

		double [] p_h = {0.1, 0.2, 0.4, 0.2, 0.1};
		
		double [] p_q_hc = {1, 0.75, 0.5, 0.25, 0};
		
		String observations = "";
		if (args.length > 0)
			observations = args[0];
		
		printResultToFile("Observation sequence Q: " + observations);
		printResultToFile("\nLength of Q: " + observations.length() + "\n");
		
		double p_q_c = 0;
		for (int i = 0; i < p_h.length; i++) {
			p_q_c += ( p_q_hc[i] * p_h[i] );
		}

		if (args.length < 1) {

			for (int i = 0; i < p_h.length; i++) {
				printResultToFile("P(h" + (i + 1) + " | Q) = " + round(p_h[i], 5) + "\n");
			}
			
			printResultToFile("\nProbability that the next candy we pick will be C, given Q: " + round(p_q_c, 5));
			printResultToFile("\nProbability that the next candy we pick will be L, given Q: " + round(1 - p_q_c, 5) + "\n\n");
			
			return;
		}
		
		calculateProbability(p_h, p_q_hc, p_q_c, (1 - p_q_c), observations);
	}
	
	private static void calculateProbability(double[] p_hc, double[] p_q_hc, double p_q_c, double p_q_l, String observSeq) {
		
		if (observSeq != null && observSeq.isEmpty())
			return;
		
		String seq = observSeq.substring(0, 1);
		double [] p_hc_next = new double[p_hc.length];
		printResultToFile("\nAfter Observation " + observSeq.charAt(0) + " = " + observSeq.substring(1, observSeq.length()) + "\n\n");
		
		for (int i = 0; i < p_hc.length; i++) {
			
			p_hc_next[i] = ((seq.equals("C") ? p_q_hc[i] : ( 1 - p_q_hc[i] )) * p_hc[i]) / (seq.equals("C") ? p_q_c : p_q_l);
			
			printResultToFile("P(h" + (i + 1) + " | Q) = " + round(p_hc_next[i], 5) + "\n");
//			System.out.format("P(h" + (i + 1) + " | Q) = %.10f\n", p_hc_next[i]);
		}
		
		double p_q_c_next = 0, p_q_l_next = 0;
		for (int i = 0; i < p_hc.length; i++) {
			
			p_q_c_next += p_q_hc[i] * p_hc_next[i];
			p_q_l_next += (1 - p_q_hc[i]) * p_hc_next[i];
		}
		
		printResultToFile("\nProbability that the next candy we pick will be C, given Q: " + round(p_q_c_next, 5));
		printResultToFile("\nProbability that the next candy we pick will be L, given Q: " + round(p_q_l_next, 5) + "\n\n");
		
//		System.out.format("\nProbability that the next candy we pick will be C, given Q: %.5f", p_q_c_next);
//		System.out.format("\nProbability that the next candy we pick will be L, given Q: %.5f\n\n", p_q_l_next);
		
		calculateProbability(p_hc_next, p_q_hc, p_q_c_next, p_q_l_next, observSeq.substring(1, observSeq.length()));
	}
	
	/**
	 * Saves the messages in memory of the Message Queuing Server to a file named "MessageQueue.txt"
	 */
	private static void printResultToFile(String resultStmt) {
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter pr = null;
		
		try {
			
			fw = new FileWriter("result.txt", true);
		    bw = new BufferedWriter(fw);
			pr = new PrintWriter(bw);
			pr.write(resultStmt);
						
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			
			try {
//				if (fw != null)
//					fw.close();
//				
//				if (bw != null)
//					bw.close();;
				
				if (pr != null)
					pr.close();
				
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		
	}
	
	private static String round(double number, int precision) {
		
		int yourScale = 5;
//        System.out.println(BigDecimal.valueOf(number).setScale(yourScale, BigDecimal.ROUND_HALF_UP));
		return BigDecimal.valueOf(number).setScale(yourScale, BigDecimal.ROUND_HALF_UP).toString();
		
	}
	
}
