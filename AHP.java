package Project;

import Project.DatabaseConnection;
import GUI.NameDisplayPage;
import java.sql.*;
import java.util.*;

public class AHP {
	private DatabaseConnection dbConnection;

	String[] languages = {"Android_Java_Kotlin", "C", "Cpp", "Rust", "Go", "iOS_Swift_Objective_C", "Java_Non_Android", "JavaScript",
			"TypeScript", "Julia", "PHP", "Python", "Ruby", "codeSQL", "Haskell", "OCaml"
	};
	List<DataObject> criteriaList = new ArrayList<>();
	List<DataObject> alternativeList = new ArrayList<>();
	List<SortObject> mapList = new ArrayList<>();
	
	public AHP() {
		input_alternatives();
		input_criteria();
		algo();
	}
	
	void input_alternatives() {
		try {
			openDatabaseConnection();
			Connection conn = dbConnection.getConnection();
			
			String command = "SELECT * FROM candidate_preferences";
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(command);
			
			
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				int[] alternatives = new int[16];
				
				for(int i=0; i<16; i++) {
					alternatives[i] = rs.getInt(languages[i]);
				}
				
				DataObject d = new DataObject(id, username, alternatives);
				alternativeList.add(d);
			}
			
			for(DataObject d : alternativeList) {
				System.out.println(d);
			}
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			closeDatabaseConnection();
		}
		
	}
	
	void input_criteria() {
		try {
			openDatabaseConnection();
			Connection conn = dbConnection.getConnection();
			
			String command = "SELECT * FROM intern_preferences";
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(command);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("position");
				int[] criteria = new int[16];
				
				for(int i=0; i<16; i++) {
					criteria[i] = rs.getInt(languages[i]);
				}
				
				DataObject d = new DataObject(id, username, criteria);
				criteriaList.add(d);
			}
			
			for(DataObject d : criteriaList) {
				System.out.println(d);
			}
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			closeDatabaseConnection();
		}
	}
	
	private void openDatabaseConnection(){
        dbConnection = new DatabaseConnection();
    }
	
	private void closeDatabaseConnection() {
        dbConnection.closeConnection();
    }
	
	void algo() {
		try {            
            int n = alternativeList.size();
            int m = criteriaList.size();
			System.out.println(n);
			double[][] criteriaPairwiseMatrix = new double[16][16];

            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    criteriaPairwiseMatrix[i][j] = (criteriaList.get(m-1).prefArr[i])/(criteriaList.get(m-1).prefArr[j]);
                }
            }

            double[][] criteriaNormalizedMatrix = new double[16][16];
            for (int i = 0; i < 16; i++) {
                double sum = 0;
                for (int j = 0; j < 16; j++) {
                    sum += criteriaPairwiseMatrix[i][j];
                }
                for (int j = 0; j < 16; j++) {
                    criteriaNormalizedMatrix[i][j] = criteriaPairwiseMatrix[i][j] / sum;
                }
            }

            double[] criteriaWeights = new double[16];
            for (int i = 0; i < 16; i++) {
                double product = 1;
                for (int j = 0; j < 16; j++) {
                    product *= criteriaNormalizedMatrix[j][i];
                }
                criteriaWeights[i] = Math.pow(product, 1.0 / 16);
            }
            
            double[][] alternativeMatrix = new double[n][16];
            
            for(int i = 0; i < n; i++) {
            	for(int j = 0; j < 16; j++) {
            		alternativeMatrix[i][j] = alternativeList.get(i).prefArr[j];
            	}
            }
            
            double[][] alternativeNormalizedMatrix = new double[n][16];
            
            for(int i = 0; i < 16; i++) {
            	double sum = 0;
            	for(int j = 0; j < n; j++) {
            		sum += alternativeMatrix[j][i];
            	}
            	for(int j = 0; j < n; j++) {
            		alternativeNormalizedMatrix[j][i] = alternativeMatrix[j][i] / sum;
            	}
            }

            double[] alternativeScores = new double[16];
            String[] alternativeNames = new String[16];
            for (int i = 0; i < n; i++) {
                double score = 0;
                for (int j = 0; j < 16; j++) {
                    score += alternativeNormalizedMatrix[i][j] * criteriaWeights[j];
                }
                alternativeScores[i] = score;
                alternativeNames[i] = alternativeList.get(i).username;
                
                SortObject s = new SortObject(alternativeNames[i], alternativeScores[i]);
				mapList.add(s);
                //sendingData.put(name, score);
            }

            System.out.println("\nPerformance scores of alternatives:");
            for (int i = 0; i < n; i++) {
                System.out.println("Alternative " + (i + 1) + ", "+ alternativeList.get(i).username +": " + alternativeScores[i]);
            }
            
            Comparator<SortObject> descendingComparator = new Comparator<SortObject>() {
                @Override
                public int compare(SortObject o1, SortObject o2) {
                    // Reverse the order by comparing o2 to o1
                    return Double.compare(o2.score, o1.score);
                }
            };

            // Sort the ArrayList in descending order using the custom comparator
            Collections.sort(mapList, descendingComparator);
            
            System.out.println();

            // Print the sorted ArrayList
            for (SortObject obj : mapList) {
                System.out.println("Username: " + obj.username + ", Score: " + obj.score);
            }
            
            String preferences[] = new String[3];
            
            for(int i=0; i<3; i++) {
            	preferences[i] = mapList.get(i).username;
            }
            
            NameDisplayPage ndp = new NameDisplayPage(preferences);
            
        } catch(Exception e) {
        	System.out.println(e);
        }
	}
	
	public static void main(String[] args) {
		AHP a = new AHP();
	}
	
	static class DataObject {
        private int id;
        private String username;
        private int[] prefArr;

        public DataObject(int id, String username, int[] prefArr) {
            this.id = id;
            this.username = username;
            this.prefArr = prefArr;
        }
        
        @Override
        public String toString() {
            String s = "DataObject{" +
                    "id=" + id +
                    ", username=" + username + ", pref=";
            
            for(int i=0; i<16; i++) {
            	s = s+prefArr[i]+",";
            }
            
            return s;
        }
    }
	
	static class SortObject {
		private String username;
		private double score;
		
		public SortObject(String username, double score) {
			this.username = username;
			this.score = score;
		}
	}

}


