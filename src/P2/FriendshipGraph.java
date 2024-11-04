package P2;

import P1.graph.ConcreteEdgesGraph;
import P1.graph.Graph;

import java.util.*;

public class FriendshipGraph {
    private final Graph<Person> graph = new ConcreteEdgesGraph<>();

    // Abstraction function:
    //   AF(graph) = 全体人员及人员间关系组成的关系图
    //
    // Representation invariant:
    //   每个顶点Person的关键字（姓名）唯一确定
    //   边不允许自达
    //   所有边的权值均为1
    //
    // Safety from rep exposure:
    //   返回的内部变量均为Immutable的数据类型
    //   类内全部属性采用private final修饰

    public FriendshipGraph() {
    }

    // checkRep
    public void checkRep(){
        Set<String> person_names = new HashSet<>();
        for(Person p: graph.vertices()){
            person_names.add(p.getName());
            for(Person p2: graph.targets(p).keySet()){
                assert graph.targets(p).get(p2) == 1;
                assert !p2.getName().equals(p.getName());
            }
        }
        assert person_names.size() == graph.vertices().size();
    }

    /**
     * Add person to FriendshipGraph as a vertex.
     *
     * @param person The person added to the FriendshipGraph as a vertex.
     *               Required to have a unique name.
     * @return true when vertex(person) added successfully.
     * false when person added doesn't have a unique name.
     */
    public boolean addVertex(Person person) {
        for (Person p : graph.vertices()) {
            if (p.getName().equals(person.getName())) return false;
        }
        boolean flag = graph.add(person);
        checkRep();
        return flag;
    }

    /**
     * Add edge from p1 to p2;
     *
     * @param p1 The object "person" where the edge starts.
     * @param p2 The object "person" where the edge ends.
     * @return true when the method works.
     */
    public boolean addEdge(Person p1, Person p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Person cannot be null!");
        }
        if (p1.getName().equals(p2.getName())) {
            IllegalArgumentException SameVertexArgument;
            SameVertexArgument = new IllegalArgumentException("Same vertex on both side of the edge!");
            throw SameVertexArgument;
        }
        graph.set(p1, p2, 1); // 有向边 from p1 to p2
        checkRep();
        return true;
    }

    /**
     * Calculate the distance from p1 to p2.
     *
     * @param p1 Object "person" where the calculation starts.
     * @param p2 Object "person" where the calculation ends.
     * @return Distance from p1 to p2, return -1 when cannot reach p2 from p1.
     */
    public int getDistance(Person p1, Person p2) {
        if (p1 == null || p2 == null)
            throw new IllegalArgumentException("Person cannot be null!");
        if (p1 == p2)
            return 0;

        int dis = 0;
        Queue<Person> queue = new LinkedList<>();
        Set<Person> isVisited = new HashSet<>();
        Person p;

        queue.offer(p1);
        while (!queue.isEmpty()) {
            dis++;
            // Record the number of person on current level.
            int n = queue.size();
            for (int i = 0; i < n; i++) {
                p = queue.poll();
                isVisited.add(p);
                if (p == null) {
                    throw new Error("Queue empty error. \nWhen the queue poll n times(n = current size of queue)" +
                            "index get out of range, resulting a null value. Please check you input and retry.");
                }
                Set<Person> targets = graph.targets(p).keySet();
                for (Person person : targets) {
                    if (!isVisited.contains(person)) {
                        System.out.println(p1.getName() + " to " + person.getName() + ":" + dis);
                        queue.offer(person);
                        isVisited.add(person);
                        if (person == p2) {
                            return dis;
                        }
                    }
                }
            }
        }
        return -1;
    }
    // Client: Main Function
    public static void main(String[] args) {
        Set<Person> persons = new HashSet<>();
        FriendshipGraph friendshipGraph = new FriendshipGraph();

        System.out.println("Class: FriendshipGraph");
        System.out.println("-------Order List-------");
        System.out.println("new name // Create a new object 'person' with name");
        System.out.println("add per name // Add a person named 'name' to the FriendshipGraph");
        System.out.println("add edg name1 name2  " +
                "// Add an undirected edge from name1 to name2 to the FriendshipGraph");
        System.out.println("dis name1 name2 // calculate distance from name1 to name2");
        System.out.println("info // get status from current FriendshipGraph");
        System.out.println("exit // exit from FriendshipGraph");
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("$ ");
            String input = scanner.nextLine();
            String[] order;
            // Order: exit
            if(input.trim().equals("exit")){
                scanner.close();
                break;
            } else if (input.trim().equals("info")) {
                // Display Set:persons
                System.out.println("Person created:");
                int cnt = 0;
                for(Person per: persons){
                    System.out.print(per.getName()+" ");
                    cnt++;
                    if(cnt%10==0) System.out.println();
                }
                System.out.println();
                // Display Set:personSet in graph
                System.out.println("Person in the graph:");
                if(friendshipGraph.graph.vertices().isEmpty())
                    System.out.println("NULL");
                else{
                    StringBuilder strb = new StringBuilder();
                    for (Person person: friendshipGraph.graph.vertices()){
                        strb.append(person.getName());
                        strb.append(", links to: ");
                        for(Person to_person:friendshipGraph.graph.targets(person).keySet()){
                            strb.append(to_person.getName()).append(", ");
                        }
                    }
                    System.out.println(strb);
                    continue;
                }
            }
            order = input.trim().split(" ");
            if(order.length==0) continue;
            try{
                // Order : new
                switch (order[0]) {
                    case "new":
                        if (order.length >= 3) {
                            System.out.println("Invalid order:" + input);
                            continue;
                        }
                        boolean flag1 = true;
                        for (Person per : persons) {
                            if (per.getName().equals(order[1])) {
                                System.out.println("You are not allowed to create a person without a unique name." +
                                        "Please retry.");
                                flag1 = false;
                                break;
                            }
                        }
                        if (flag1) {
                            Person newPerson = new Person(order[1]);
                            persons.add(newPerson);
                            System.out.println(order[1] + " created.");
                        }

                        break;
                    // Order : add per/edg
                    case "add":
                        if (order[1].equals("per")) {
                            if (order.length >= 4) {
                                System.out.println("Invalid order:" + input);
                                continue;
                            }
                            boolean flag2 = true;
                            for (Person per : persons) {
                                if (per.getName().equals(order[2])) {
                                    friendshipGraph.addVertex(per);
                                    System.out.println(per.getName() + " added.");
                                    flag2 = false;
                                }
                            }
                            if (flag2) {
                                System.out.println("Cannot find person named " + order[2] + ".");
                            }
                        } else if (order[1].equals("edg")) {
                            if (order.length >= 5) {
                                System.out.println("Invalid order:" + input);
                                continue;
                            }
                            Person p1 = null, p2 = null;
                            for (Person per : persons) {
                                if (per.getName().equals(order[2])) p1 = per;
                                if (per.getName().equals(order[3])) p2 = per;
                                if (p1 != null && p2 != null) break;
                            }
                            if (p1 == null||!friendshipGraph.graph.vertices().contains(p1)) {
                                System.out.println("Cannot find person 1 named " + order[2] + " in the graph.");
                                continue;
                            }
                            if (p2 == null||!friendshipGraph.graph.vertices().contains(p2)) {
                                System.out.println("Cannot find person 2 named " + order[3] + " in the graph.");
                                continue;
                            }
                            if(p1==p2){
                                System.out.println("They are the same person!");
                                continue;
                            }
                            friendshipGraph.graph.set(p1,p2,1);
                            friendshipGraph.graph.set(p2,p1,1);
                            System.out.println("Edge:" + p1.getName() + "-" + p2.getName() + " added.");
                        } else {
                            System.out.println("Invalid order:" + input);
                        }
                        break;
                    // Order: dis
                    case "dis":
                        if (order.length >= 4) {
                            System.out.println("Invalid order:" + input);
                            continue;
                        }
                        Person p1 = null, p2 = null;
                        for (Person per : persons) {
                            if (per.getName().equals(order[1])) p1 = per;
                            if (per.getName().equals(order[2])) p2 = per;
                            if (p1 != null && p2 != null) break;
                        }
                        if (p1 == null||!friendshipGraph.graph.vertices().contains(p1)) {
                            System.out.println("Cannot find person 1 named " + order[1] + " in the graph.");
                            continue;
                        }
                        if (p2 == null||!friendshipGraph.graph.vertices().contains(p2)) {
                            System.out.println("Cannot find person 2 named " + order[2] + " in the graph.");
                            continue;
                        }
                        int dis = friendshipGraph.getDistance(p1, p2);
                        if (dis == -1) {
                            System.out.println(p1.getName() + " and " + p2.getName() + " is not connected!");
                        } else {
                            System.out.print("The distance from " + p1.getName() + " to " + p2.getName() + " is:");
                            System.out.println(dis);
                        }
                        break;
                    default:
                        System.out.println("Invalid order:" + input);
                        break;
                }
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Invalid order format:"+ input);
            }
        }
    }
}
