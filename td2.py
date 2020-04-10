def list_to_matrix(L):
    # This assumes that L is an array of linked lists of pairs (index, weight)
    M = new matrix of dimension |L|
    Initialize M with zeros
    for i from 0 to dim -1:
        for elem in L[i]
            M[i][elem.index] = elem.weight
    return M


def matrix_to_list(M):
    L = new list of size |M|
    Initialize L with empty linked lists
    for i from 0 to |M| -1:
        for j from 0 to |M| -1:
            if M[i][j]:
                L[i].push(j)
    return L  