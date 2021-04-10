data class Node(
    val value: Double? = null,
    val operator: Char? = null,
    var leftChild: Node? = null,
    var rightChild: Node? = null
) {
    fun isLeaf(): Boolean = operator == null
}