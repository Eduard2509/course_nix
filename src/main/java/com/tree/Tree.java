package com.tree;

import com.model.Vehicle;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Stack;

@Getter
@Setter
public class Tree<T extends Vehicle> {

    private Node rootTree;

    @Getter
    @Setter
    private class Node {

        private Vehicle value;
        private Node left;
        private Node right;

        public Node(Vehicle value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public Node(Vehicle value) {
            this.value = value;
        }

        public Node() {

        }
    }

    public void add(Vehicle vehicle) {
        Node newNode = new Node();
        newNode.setValue(vehicle);
        if (rootTree == null) {
            rootTree = newNode;
        } else {
            Node currentNode = rootTree;
            Node parentNode;
            while (true) {
                parentNode = currentNode;
                if (comparator.compare(vehicle, currentNode.getValue()) == 0) {
                    return;
                } else if ((comparator.compare(vehicle, currentNode.getValue())) == 1) {
                    currentNode = currentNode.getLeft();
                    parentNode.setLeft(newNode);

                    if (currentNode == null) {
                        return;
                    }
                } else {
                    currentNode = currentNode.getRight();
                    if (currentNode == null) {
                        parentNode.setRight(newNode);
                        return;
                    }
                }
            }
        }
    }


    public void printTree() {
        Stack globalStack = new Stack();
        globalStack.push(rootTree);
        int emptyLeaf = 32;
        boolean isRowEmpty = false;
        System.out.println("****......................................................****");
        while (isRowEmpty == false) {

            Stack localStack = new Stack();
            isRowEmpty = true;

            for (int j = 0; j < emptyLeaf; j++)
                System.out.print(' ');

            while (globalStack.isEmpty() == false) {
                Node temp = (Node) globalStack.pop();
                if (temp != null) {
                    System.out.print("Price: " + temp.value.getPrice() + " " + temp.value.getModel());
                    localStack.push(temp.left);
                    localStack.push(temp.right);
                    if (temp.left != null || temp.right != null)
                        isRowEmpty = false;
                } else {
                    System.out.print("--");
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j = 0; j < emptyLeaf * 2 - 2; j++)
                    System.out.print(' ');
            }
            System.out.println();
            emptyLeaf /= 2;
            while (localStack.isEmpty() == false)
                globalStack.push(localStack.pop());
        }
        System.out.println("****......................................................****");
    }


    public BigDecimal sumRightBranch() {
        return sum(rootTree.right).add(rootTree.value.getPrice());
    }

    public BigDecimal sumLeftBranch() {
        return sum(rootTree.left).add(rootTree.value.getPrice());
    }

    private BigDecimal sum(Node node) {
        if (node == null) {
            return BigDecimal.ZERO;
        }
            return sum(node.left).add(node.value.getPrice().add(sum(node.right)));

    }

    final Comparator<Vehicle> comparator = Comparator.comparing(Vehicle::getPrice)
            .reversed()
            .thenComparing(vehicle -> vehicle.getClass().getSimpleName())
            .thenComparing(Vehicle::getCount);
}
