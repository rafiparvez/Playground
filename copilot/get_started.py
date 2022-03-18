def get_matrix_transpose(matrix):
    """
    Function to get the transpose of a matrix.
    :param matrix: The matrix to get the transpose of.
    :return: The transpose of the matrix.
    """
    return [[matrix[j][i] for j in range(len(matrix))] for i in range(len(matrix[0]))]

def minimum_edit_distance_between_words(word1, word2):
    """
    """
    if len(word1) > len(word2):
        word1, word2 = word2, word1
    distances = range(len(word1) + 1)
    for index2, char2 in enumerate(word2):
        newDistances = [index2 + 1]
        for index1, char1 in enumerate(word1):
            if char1 == char2:
                newDistances.append(distances[index1])
            else:
                newDistances.append(1 + min((distances[index1], distances[index1 + 1], newDistances[-1])))
        distances = newDistances
    return distances[-1]



    