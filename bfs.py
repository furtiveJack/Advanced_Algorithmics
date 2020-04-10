def breadth_first(tree):
    to_traverse = queue.new()
    to_traverse.push(tree.root)
    while to_traverse is not empty:
        elt = to_traverse.dequeue()
        for child in elt.children
            to_traverse.push(child)
        print(elt)
    return

-> depth_first search just by using this algorithm with a stack instead of a queue

def bubble_sort(array):
    for i in range(len(array)-1, 1, -1):
        sorted = True
        for j in range(0, i-1):
            if array[j+1] < array[j]:
                array[j+1], array[j] = array[j], array[j+1]
        if sorted:
            return

"""
    a   b   c   d   e   f   g

a   0   1   1   0   0   0   0

b   0   1   1   0   0   0   0

c   0   0   0   1   0   0   0

d   0   0   1   0   0   0   0

e   0   0   0   1   0   1   0

f   0   0   0   0   0   0   0

g   0   0   0   0   1   1   1



A |_| -> B -> C

B |_| -> B -> C

C |_| -> D

D |_| -> C

E |_| -> D -> F

F |_| -> 

G |_| -> E -> F -> G

"""