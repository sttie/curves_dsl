import javax.management.ObjectInstance
import javax.management.OperationsException

class IntegerWrapper(initNodeValue: Double? = null) {
    companion object {
        val emptyValueNode = Node()

        fun operatorWithIntegerWrapper(
            leftOperand: IntegerWrapper, operator: Char,
            rightOperand: IntegerWrapper
        ): IntegerWrapper {
            val newWrapper = IntegerWrapper()

            val newNode = Node(operator = operator, leftChild = leftOperand.root,
                               rightChild = rightOperand.root)
            newWrapper.root = newNode

            return newWrapper
        }
    }

    var root: Node = emptyValueNode
    init {
        root = if (initNodeValue == null) emptyValueNode else Node(value = initNodeValue)
    }

    // Эх, сейчас бы шаблонную магию из С++... :(
    operator fun plus(x: Double): IntegerWrapper {
        return operatorWithIntegerWrapper(this, '+', IntegerWrapper(x))
    }

    operator fun plus(x: Int): IntegerWrapper {
        return plus(x.toDouble())
    }

    operator fun plus(x: IntegerWrapper): IntegerWrapper {
        return operatorWithIntegerWrapper(this, '+', x)
    }

    operator fun minus(x: Double): IntegerWrapper {
        return operatorWithIntegerWrapper(this, '-', IntegerWrapper(x))
    }

    operator fun minus(x: Int): IntegerWrapper {
        return minus(x.toDouble())
    }

    operator fun minus(x: IntegerWrapper): IntegerWrapper {
        return operatorWithIntegerWrapper(this, '-', x)
    }

    operator fun times(x: Double): IntegerWrapper {
        return operatorWithIntegerWrapper(this, '*', IntegerWrapper(x))
    }

    operator fun times(x: Int): IntegerWrapper {
        return times(x.toDouble())
    }

    operator fun times(x: IntegerWrapper): IntegerWrapper {
        return operatorWithIntegerWrapper(this, '*', x)
    }

    operator fun div(x: Double): IntegerWrapper {
        return operatorWithIntegerWrapper(this, '/', IntegerWrapper(x))

    }

    operator fun div(x: Int): IntegerWrapper {
        return div(x.toDouble())
    }

    operator fun div(x: IntegerWrapper): IntegerWrapper {
        return operatorWithIntegerWrapper(this, '*', x)
    }


    fun evaluate(tValue: Double): Double {
        if (root == emptyValueNode)
            return tValue

        fun dfs(currentNode: Node): Double {
            if (currentNode.isLeaf())
                return currentNode.value ?: tValue

            var result = if (currentNode.operator in listOf('+', '-')) 0.0 else 1.0
            if (currentNode.leftChild != null)
                result = dfs(currentNode.leftChild!!)
            if (currentNode.rightChild != null)
                result = when (currentNode.operator) {
                    '+' -> result + dfs(currentNode.rightChild!!)
                    '-' -> result - dfs(currentNode.rightChild!!)
                    '*' -> result * dfs(currentNode.rightChild!!)
                    '/' -> result / dfs(currentNode.rightChild!!)
                    else -> throw OperationsException("Unknown operation!")
                }

            return result
        }

        return dfs(root)
    }
}


operator fun Int.plus(wrapper: IntegerWrapper): IntegerWrapper {
    return wrapper + this.toDouble()
}

operator fun Int.minus(wrapper: IntegerWrapper): IntegerWrapper {
    return IntegerWrapper(this.toDouble()) - wrapper
}

operator fun Int.times(wrapper: IntegerWrapper): IntegerWrapper {
    return wrapper * this.toDouble()
}

operator fun Int.div(wrapper: IntegerWrapper): IntegerWrapper {
    return IntegerWrapper(this.toDouble()) / wrapper
}

operator fun Double.plus(wrapper: IntegerWrapper): IntegerWrapper {
    return wrapper + this
}

operator fun Double.minus(wrapper: IntegerWrapper): IntegerWrapper {
    return IntegerWrapper(this) - wrapper
}

operator fun Double.times(wrapper: IntegerWrapper): IntegerWrapper {
    return wrapper * this
}

operator fun Double.div(wrapper: IntegerWrapper): IntegerWrapper {
    return IntegerWrapper(this) / wrapper
}

fun Int.toIntegerWrapper(): IntegerWrapper {
    return IntegerWrapper(this.toDouble())
}

fun Double.toIntegerWrapper(): IntegerWrapper {
    return IntegerWrapper(this)
}