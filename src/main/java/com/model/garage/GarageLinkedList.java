package com.model.garage;

import com.model.Vehicle;
import com.service.VehicleService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;


public class GarageLinkedList<K extends Vehicle> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GarageLinkedList.class);

    private Node<K> currentPointer;
    private Node<K> head;
    private Node<K> lastNode;
    public Iterator<K> iterator = new Iterator<K>() {

        @Override
        public boolean hasNext() {
            if (head == null) {
                return false;
            }
            if (currentPointer == null) {
                currentPointer = head;
            }
            return currentPointer.nextElement != null;
        }

        @Override
        public K next() {
            if (head == null) {
                return null;
            }
            if (currentPointer == null) {
                currentPointer = head;
            }
            K vehicle = currentPointer.getVehicle();
            currentPointer = currentPointer.nextElement;
            return vehicle;
        }
    };

    public void forEach() {
        while (iterator.hasNext()) {
            System.out.println(currentPointer.getVehicle());
            iterator.next();
        }
        if (currentPointer != null) {
            System.out.println(currentPointer.getVehicle());
        }
    }


    @Getter
    @Setter
    private class Node<K> {
        private K vehicle;
        private Node<K> nextElement;
        private Node<K> prevElement;
        private String restyling;
        private String time;


        private Node(Node<K> prevElement, K vehicle, Node<K> nextElement) {
            this.vehicle = vehicle;
            this.nextElement = nextElement;
            this.prevElement = prevElement;
        }

        private Node() {
            this.restyling = UUID.randomUUID().toString();
            this.time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        }

    }


    public void add(K vehicle) {
        Node<K> newNode = new Node<>();
        newNode.setVehicle(vehicle);

        Node<K> last = getLastNode();
        if (last == null) {
            head = newNode;
            return;
        }
        last.setNextElement(newNode);
        newNode.setPrevElement(last);
        LOGGER.info("Vehicle add to list {}", vehicle.getId());
    }

    public void addHead(K vehicle) {
        Node<K> node = new Node<K>();
        node.setVehicle(vehicle);
        node.setPrevElement(null);
        if (head != null) {
            node.nextElement = head;
        }
        this.head = node;
        LOGGER.info("Vehicle add to list {}", vehicle.getId());
    }

    private Node<K> getLastNode() {
        if (head == null) {
            return null;
        }
        Node<K> current = head;
        while (current.nextElement != null) {
            current = current.nextElement;
        }
        return current;
    }


    public Optional<Vehicle> getVehicleByRestyling(String restyling) {
        Node<K> current = this.head;
        while (current != null) {
            if (current.getVehicle().getRestyling().equals(restyling)) {
                return Optional.of(current.getVehicle());
            }
            current = current.nextElement;
        }
        return Optional.empty();
    }

    private K unlinked(Node<K> node) {
        K vehicle = node.vehicle;
        Node<K> prev = node.prevElement;
        Node<K> next = node.nextElement;
        if (prev == null) {
            head = next;
        } else {
            prev.nextElement = next;
            node.prevElement = null;
        }

        if (next == null) {
            lastNode = prev;
        } else {
            next.prevElement = prev;
            node.nextElement = null;
        }

        node.vehicle = null;

        return vehicle;
    }

    public boolean deleteVehicleByRestyling(String restyling) {
        Optional<Node<K>> current = Optional.of(this.head);
        while (!current.isEmpty() && !current.get().getVehicle().getRestyling().equals(restyling)) {
            current = Optional.of(current.get().nextElement);
        }
        if (current == null) {
            for (Node<K> x = head; x != null; x = x.nextElement) {
                if (x.vehicle == null) {
                    unlinked(x);
                    return true;

                }
            }
        } else {
            for (Node<K> x = head; x != null; x = x.nextElement) {
                if (current.get().vehicle.equals(x.vehicle)) {
                    unlinked(x);
                    return true;
                }
            }
        }
        return false;
    }


    public Optional<Node<K>> update(K toVehicle, K fromVehicle) {
        Node<K> current = this.head;
        while (current != null && !current.getVehicle().equals(toVehicle)) {
            current = current.nextElement;
        }
        if (current.getVehicle() == null) {
            System.out.println("Collection is empty");
        }
        current.setVehicle(fromVehicle);
        LOGGER.info("Vehicle update {}", current.vehicle.getId());
        return Optional.of(current);

    }


    public int sumRestyling(K vehicle) {
        int sum = 0;
        Node<K> current = this.head;
        while (current != null) {
            if (current.getVehicle().getManufacturer().equals(vehicle.getManufacturer())) {
                sum++;
            }
            current = current.nextElement;
        }
        return sum;
    }


    public String getDateFirstRestyling() {
        Node<K> current = head;
        if (head == null) {
            return "Garage is empty";
        }
        return current.getVehicle().getTime();
    }

    public String getDateLastRestyling() {
        if (head == null) {
            return "Garage is empty";
        } else {
            Node<K> current = head;
            while (true) {
                if (current.nextElement == null) {
                    return current.getVehicle().getTime();
                }
                current = current.nextElement;
            }
        }
    }


    private Node<K> getNext(Node<K> current) {
        return current.getNextElement();
    }


    public void printAll() {
        if (head == null) {
            System.out.println("List is empty");
            return;
        }
        Node<K> target = head;
        while (target != null) {
            System.out.println(target.getVehicle());
            target = getNext(target);
        }
    }

}
