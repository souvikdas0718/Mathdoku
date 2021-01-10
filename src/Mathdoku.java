import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/*
 * @desc This Class stores the information of the Graph.
 * @attribute HashMap<Character, ArrayList<ArrayList<Integer>>> hashKeys to store each groups indexes
 * @attribute HashMap<String, String> mathdokuOperand to store each unique vertex of the graph
 * @attribute HashMap<ArrayList<Integer>, Integer> mathdokuPuzzle
 * @attribute gridLengthMax keeps a count on the maximum length of puzzle
 * @attribute count_Wrong_Choices keeps a count the number of wrong choices made
 * @author souvikdas
 */
public class Mathdoku {

    public HashMap<Character, ArrayList<ArrayList<Integer>>> hashKeys;
    public HashMap<String, String> mathdokuOperand;
    public HashMap<ArrayList<Integer>, Integer> mathdokuPuzzle;
    int gridLengthMax;
    int count_Wrong_Choices;

    /*
     * @Constructor SuperClass of Mathdoku class
     */
    public Mathdoku(){
        hashKeys = new HashMap<>();
        mathdokuOperand = new HashMap<>();
        mathdokuPuzzle = new HashMap<>();
        gridLengthMax = 0;
        count_Wrong_Choices = 0;
    }

    /*
     * This method loadPuzzle is used to read the puzzle and its cell grouping operations
     *
     * @attribute sRead parameter to read the file line by line
     * @param stream  parameter which reads data from the input file sent by the user
     * @attribute first assigning flag for finding the length of the puzzle
     * @attribute gridLength  Third parameter which is assigned as weight between the vertices
     * @return true in case successful correct inputs passed
     */
    public boolean loadPuzzle(BufferedReader stream) {
        String sRead;
        boolean first = true;
        int gridLength = 0;
        hashKeys.clear();
        mathdokuOperand.clear();
        mathdokuPuzzle.clear();
        try {
            while (((sRead = stream.readLine()) != null)) {
                //Handling conditions for null or empty input lines
                if(sRead.matches("[ ]+")){
                    continue;
                }
                if(sRead.isEmpty()){
                    continue;
                }
                //Removing white spaces from the input list and assigning the maximum grid length of the puzzle by the first element entry
                sRead = sRead.trim();
                if (first) {
                    gridLengthMax = sRead.length();
                    first = false;
                }

                //Code to add the groups in hashkeys HashMap according to their indexes
                if (gridLength < gridLengthMax && sRead.length()==gridLengthMax) {
                    if(sRead.length()==gridLengthMax && sRead.contains(" ")){
                        return false;
                    }
                    char[] charArray = sRead.toCharArray();
                    int pos = 0;
                    for (char gridGroup : charArray) {
                        ArrayList<ArrayList<Integer>> temp1 = new ArrayList<>();
                        ArrayList<Integer> arr = new ArrayList<>();
                        arr.add(gridLength);
                        arr.add(pos);
                        temp1.add(arr);
                        if (!hashKeys.containsKey(gridGroup)) {
                            hashKeys.put(gridGroup, temp1);
                        } else {
                            ArrayList<Integer> arr2 = new ArrayList<>();
                            arr2.add(gridLength);
                            arr2.add(pos);
                            ArrayList<ArrayList<Integer>> temp = hashKeys.get(gridGroup);
                            temp.add(arr2);
                            hashKeys.put(gridGroup, temp);
                        }
                        pos++;
                    }
                    gridLength++;
                } else {
                    // after adding the puzzle groups now we adding the individual cell grouping to mathdokuOperand HashMap
                    //Removing the extra spaces from the
                    sRead = sRead.trim().replaceAll("[ ]{2,}", " ");
                    String[] arr = sRead.split(" ");

                    //Validation for cell grouping parameters if the length of the cell group string consists of more than 3 elements return false
                    if (arr.length != 3) {
                        hashKeys.clear();
                        return false;
                    }
                    else if(!arr[1].matches("[0-9]+") || !arr[2].matches("[-+/*=]{1}")){
                        hashKeys.clear();
                        return false;
                    } else{
                        int count = 0;
                        String keyReference = "";
                        StringBuilder operand = new StringBuilder();
                        for (String element : arr) {
                            if (count == 0) {
                                keyReference = element;
                                count++;
                            } else {
                                operand.append(element);
                            }
                        }
                        operand.append(" ").append(keyReference);
                        if (mathdokuOperand.containsKey(operand.toString())) {
                            keyReference += " " + mathdokuOperand.get(operand.toString());
                        }
                        mathdokuOperand.put(operand.toString(), keyReference);

                    }
                }
            }
        } catch (IOException e) {
            //To handle Input Output Exceptions while reading the file
            //e.printStackTrace();
            return false;
        }
        return mathdokuOperand.size()>0;
    }

    /*
     * This method print is used to print the puzzle and its cell grouping operations
     * @attribute StringBuilder str is used to append the values obtained in the mathdokuPuzzle based on the index values
     * @return the mathdokuPuzzle in the for of string
     */
    public String print() {
        if(gridLengthMax==0){
            return null;
        }
        if(!readyToSolve()){
            return null;
        }

        Character[][] c = new Character[gridLengthMax][gridLengthMax];

        ArrayList<ArrayList<Integer>> al = new ArrayList<>();
        for (Character str2:hashKeys.keySet()) {
            al = hashKeys.get(str2);
            for (ArrayList<Integer> al2 :  al) {
                c[al2.get(0)][al2.get(1)] = str2;
            }
        }

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < gridLengthMax; i++) {
            for (int j = 0; j < gridLengthMax; j++) {
                ArrayList<Integer> position = new ArrayList<>();
                position.add(i);
                position.add(j);
                if (mathdokuPuzzle.get(position) != null)
                    str.append(mathdokuPuzzle.get(position));
                else {
                    str.append(c[i][j]);
                    //return null;
                }
            }
            str.append("\n");
        }
        return str.toString();
    }

    /*
     * This method readyToSolve is used to check if the puzzle is solvable by checking for its validations
     * @attribute count keeps a count of the groups indexes in hashKeys HashMap
     * @return true if the count of the group index sizes are equal to gridLengthMax+ gridLengthMax
     */
    public boolean readyToSolve() {
        int count = 0;
        boolean flag = true;
        if(gridLengthMax == 0){
            flag = false;
        }
        else {
            for (Character character : hashKeys.keySet()) {
                count += hashKeys.get(character).size();
            }
            if (count == (gridLengthMax * gridLengthMax)){
                flag = true;
            }
            else
                flag = false;
        }
        if(!flag){
            hashKeys.clear();
            mathdokuOperand.clear();
            mathdokuPuzzle.clear();
        }
        return flag;
    }

    /*
     * This method solve is used to solve the mathdoku puzzle
     * @method solveRecursively keeps a count of the groups indexes in hashKeys HashMap
     * @return true if the puzzle is solved recursively
     */
    public boolean solve() {
        //Initialising the choices to null before calling solve method for recursive calls
        count_Wrong_Choices=0;
        if(!readyToSolve()){
            hashKeys.clear();
            mathdokuOperand.clear();
            mathdokuPuzzle.clear();
            return false;
        }
        solveRecursively(new ArrayList<>(mathdokuOperand.keySet()), 0);
        //System.out.println(solveStatus);
        return true;
    }

    /*
     * This method solveRecursively is used to perform the solve method recursively
     * @param ArrayList<String> strs stores all the possible keySet from mathdokuOperand HashMap where each keySet is indexed based on number sequence
     * @attribute groupingElement stores the grouping Element string
     * @return true in case successful recursion else returns false to the solve method
     */
    public boolean solveRecursively(ArrayList<String> strs, int idx) {
        if (idx == strs.size()) {
            return true;
        }
        String strKeys = strs.get(idx);
        String groupingElement;

        //Replacing the keys having "=" opration to "+" operation
        if (strKeys.contains("=")) {
            mathdokuOperand.put(strKeys.replace("=", "+"), mathdokuOperand.get(strKeys));
            strKeys = strKeys.replace("=", "+");
        }

        //Based on the operation we select the groupingElement by passing strKeys to mathdokuOperand HashMap
        if (strKeys.contains("+")) {
            groupingElement = mathdokuOperand.get(strKeys);
        } else if (strKeys.contains("-")) {
            groupingElement = mathdokuOperand.get(strKeys);
        } else if (strKeys.contains("*")) {
            groupingElement = mathdokuOperand.get(strKeys);
        } else if (strKeys.contains("/")) {
            groupingElement = mathdokuOperand.get(strKeys);
        } else {
            return false;
        }

        //groupingElement = groupingElement.trim();

        /*We are obtaining the ArrayList<ArrayList<Integer>> indexes for the groupingElements from hashKeys HashMap and generate efficient
        .. combination based on the computational value of the groupOperation*/
        ArrayList<ArrayList<Integer>> indexes = hashKeys.get(groupingElement.charAt(0));
        ArrayList<String> efficientCombination = combinations(generateCombinations(indexes), strKeys);
        ArrayList<Integer> row = new ArrayList<>();
        ArrayList<Integer> column = new ArrayList<>();

        //Checking for the ambiguity of each combination with the mathdokuPuzzle row and columns. In case of ambiguity the next combinations are checked.
        for (String eCombos : efficientCombination) {
            boolean flag = true;
            int i = 0;

            //Checking for each combination values iteratively
            String[] eCombo = eCombos.split(" ");

            //Checking row wise ambiguity
            for (ArrayList<Integer> locations : indexes) {
                for (int j = 0; j < gridLengthMax; j++) {
                    row.add(locations.get(0));
                    row.add(j);
                    if (mathdokuPuzzle.get(row) != null && mathdokuPuzzle.get(row) == Integer.parseInt(eCombo[i])) {
                        flag = false;
                        row.clear();
                        break;
                    }
                    row.clear();
                }
                //In case of row ambiguity check for different combination
                if (!flag) {
                    break;
                }
                //Checking row wise ambiguity
                for (int k = 0; k < gridLengthMax; k++) {
                    column.add(k);
                    column.add(locations.get(1));
                    if (mathdokuPuzzle.get(column) != null && mathdokuPuzzle.get(column) == Integer.parseInt(eCombo[i])) {
                        flag = false;
                        column.clear();
                        break;
                    }
                    column.clear();
                }
                //In case of row ambiguity check for different combination
                if (!flag) {
                    break;
                }

                /* In case there is no ambiguity in the mathdokuPuzzle for the given element eCombo[i], put the values in the puzzle
                .. wrt. their locations and check for next eCombo element */
                mathdokuPuzzle.put(locations, Integer.parseInt(eCombo[i]));
                i++;
            }
            //In case of any ambiguity in eCombo for the given combination remove all the inserted elements corresponding to the location from mathdokuPuzzle HashMap
            if (!flag) {
                for (ArrayList<Integer> locations : indexes)
                    mathdokuPuzzle.put(locations, null);
                continue;
            }

            //Checking row and column wise ambiguity in among the group in mathdokuPuzzle
            for (ArrayList<Integer> locations : indexes) {
                int puzzleIndexValue = mathdokuPuzzle.get(locations);
                for (int itr = 0; itr < gridLengthMax; itr++) {
                    if (itr == locations.get(1))
                        continue;
                    row.add(locations.get(0));
                    row.add(itr);
                    if (mathdokuPuzzle.get(row) != null && mathdokuPuzzle.get(row) == puzzleIndexValue) {
                        flag = false;
                        row.clear();
                        break;
                    }
                    row.clear();
                }
                if (!flag)
                    break;
                for (int k = 0; k < gridLengthMax; k++) {
                    if (k == locations.get(0))
                        continue;
                    column.add(k);
                    column.add(locations.get(1));
                    if (mathdokuPuzzle.get(column) != null && mathdokuPuzzle.get(column) == puzzleIndexValue) {
                        flag = false;
                        column.clear();
                        break;
                    }
                    column.clear();
                }
                if (!flag)
                    break;
            }
            //If group wise row-column ambiguity fails then remove all the inserted elements corresponding to the location from mathdokuPuzzle HashMap
            if (!flag) {
                for (ArrayList<Integer> locations : indexes)
                    mathdokuPuzzle.put(locations, null);
                continue;
            }

            // Recursively call the solveRecursively method for the next iteration in ArrayList of keySet from mathdokuOperand HashMap
            flag = solveRecursively(strs, idx + 1);

            //If solveRecursively method returns true in all the above cases for the given combination the return true
            if (flag) {
                return true;
            }
            //else backtrack the puzzle for different combination and increment the count_Wrong_Choices counter by one
            else {
                for (ArrayList<Integer> locations : indexes) {
                    mathdokuPuzzle.put(locations, null);
                }
                count_Wrong_Choices++;
            }

        }
        return false;
    }

    /*
     * This method choices is used to return the count of wrong choices during the backtracking recursive calls
     * @return count_Wrong_Choices integer value to the calling method
     */
    public int choices( ){
        return count_Wrong_Choices;
    }

    /*
     * This method returns all the combinations from the given list of indexes
     * @param ArrayList<ArrayList<Integer>> indexes stores the indexes from the hashKeys HashMap for various groupingElements
     * @attribute ArrayList<ArrayList<Integer>> combinations stores the list of combinations
     * @return combinations to the calling method
     */
    public ArrayList<ArrayList<Integer>> generateCombinations(ArrayList<ArrayList<Integer>> indexes) {
        ArrayList<ArrayList<Integer>> combinations = new ArrayList<>();
        for (ArrayList<Integer> ignored : indexes) {
            ArrayList<Integer> combos = new ArrayList<>();
            for (int i = 1; i <= gridLengthMax; i++) {
                combos.add(i);
            }
            combinations.add(combos);
        }
        return combinations;
    }

    /*
     * This method returns all the combinations from the given list of indexes
     * @param ArrayList<ArrayList<Integer>> combinationList stores all combinations passed by the generateCombination method
     * @param String str stores the keySets of mathdokuOperand HashMap
     * @attribute ArrayList<ArrayList<Integer>> combinations stores the list of combinations
     * @methodCall generatePermutations method is used to generate possible combinations based on the valid operations
     * @return combinations to the calling method
     */
    public ArrayList<String> combinations(ArrayList<ArrayList<Integer>> combinationList, String str) {
        ArrayList<String> combinations = new ArrayList<>();
        String old_str = str.split(" ")[0];
        generatePermutations(combinationList, combinations, 0, "", Integer.parseInt(old_str.substring(0, old_str.length() - 1)), old_str.substring(old_str.length() - 1));
        return combinations;
    }

    /*
     * This method returns all the combinations from the given list of indexes
     * @param ArrayList<ArrayList<Integer>> lists stores the indexes from the hashKeys HashMap for various groupingElements
     * @param ArrayList<String> result stores the valid combinations based on the switch case computation
     * @param depth stores the depth of the level of combinations based on the size of group indexes
     * @param current stores the combination values based on the group indexes size
     * @param expected stores the expected operation value for the current group
     * @param operation stores the computational operation
     * @methodCall sAddition computes addition of the current value passed and compare with expected value. If valid, the combination is added to ArrayList<String> result
     * @methodCall sMultiplication computes multiplication of the current value passed and compares with expected value. If valid, the combination is added to ArrayList<String> result
     * @methodCall sSubtraction computes subtraction of the current value passed and compares with expected value. If valid, the combination is added to ArrayList<String> result
     * @methodCall sDivision computes division of the current value passed and compares with expected value. If valid, the combination is added to ArrayList<String> result
     * @Referred://https://stackoverflow.com/questions/17192796/generate-all-combinations-from-multiple-lists
     */
    public void generatePermutations(ArrayList<ArrayList<Integer>> lists, ArrayList<String> result, int depth, String current, int expected, String operation) {
        if (depth == lists.size()) {
            switch (operation) {
                case "+":
                    if (sAddition(current, expected)) {
                        result.add(current.trim());
                    }
                    break;
                case "*":
                    if (sMultiplication(current, expected)) {
                        result.add(current.trim());
                    }
                    break;
                case "-":
                    if (sSubtraction(current, expected)) {
                        result.add(current.trim());
                    }
                    break;
                case "/":
                    if (sDivision(current, expected)) {
                        result.add(current.trim());
                    }
                    break;
            }
            return;
        }
        //While depth!=list.size() we increase the depth and recursively call the generatePermutations method
        for (int i = 0; i < lists.get(depth).size(); i++) {
            generatePermutations(lists, result, depth + 1, current + " " + lists.get(depth).get(i), expected, operation);
        }
    }

    /*
     * This method performs addition
     * @param current stores the combination values based on the group indexes size
     * @param expected stores the expected operation value for the current group
     * @attribute sum to compute the current elements
     * @return boolean value based on comparision between sum and expected values
     */
    private boolean sAddition(String current, int expected) {
        String[] elements = current.trim().split(" ");
        int sum = 0;
        int i = 0;
        while (i < elements.length) {
            sum += Integer.parseInt(elements[i]);
            i++;
        }
        return expected == sum;
    }

    /*
     * This method performs multiplication
     * @param current stores the combination values based on the group indexes size
     * @param expected stores the expected operation value for the current group
     * @attribute mul to compute the current elements
     * @return boolean value based on comparision between mul and expected values
     */
    private boolean sMultiplication(String current, int expected) {
        String[] elements = current.trim().split(" ");
        int mul = 1;
        int i = 0;
        while (i < elements.length) {
            mul *= Integer.parseInt(elements[i]);
            i++;
        }
        return expected == mul;
    }

    /*
     * This method performs subtraction
     * @param current stores the combination values based on the group indexes size
     * @param expected stores the expected operation value for the current group
     * @attribute sub to compute the current elements
     * @return boolean value based on comparision between sub and expected values
     */
    private boolean sSubtraction(String current, int expected) {
        String[] elements = current.trim().split(" ");
        int sub;
        int i = 0;
        if (Integer.parseInt(elements[i]) > Integer.parseInt(elements[i + 1])) {
            sub = Integer.parseInt(elements[i]) - Integer.parseInt(elements[i + 1]);
        } else {
            sub = Integer.parseInt(elements[i + 1]) - Integer.parseInt(elements[i]);
        }
        return expected == sub;
    }

    /*
     * This method performs division
     * @param current stores the combination values based on the group indexes size
     * @param expected stores the expected operation value for the current group
     * @attribute div to compute the current elements
     * @return boolean value based on comparision between div and expected values
     */
    private boolean sDivision(String current, int expected) {
        String[] elements = current.trim().split(" ");
        int div = 1;
        int i = 0;
        if (Integer.parseInt(elements[i]) > Integer.parseInt(elements[i + 1])) {
            if (Integer.parseInt(elements[i]) % Integer.parseInt(elements[i + 1]) == 0)
                div = Integer.parseInt(elements[i]) / Integer.parseInt(elements[i + 1]);
        } else {
            if (Integer.parseInt(elements[i + 1]) % Integer.parseInt(elements[i]) == 0)
                div = Integer.parseInt(elements[i + 1]) / Integer.parseInt(elements[i]);
        }
        return expected == div;
    }
}