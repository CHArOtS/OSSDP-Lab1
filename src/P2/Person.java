package P2;

public class Person {
    private final String name;

    public Person(String newname) {
        this.name = newname;
    }
    public String getName() {
        return name;
    }
    @Override
    public boolean equals(Object object){
        if(this==object) return true;
        if(object==null || this.getClass()!=object.getClass()) return false;
        Person person = (Person) object;
        return this.name.equals(person.name);
    }
}
