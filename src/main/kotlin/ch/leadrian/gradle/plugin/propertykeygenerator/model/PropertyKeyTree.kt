package ch.leadrian.gradle.plugin.propertykeygenerator.model

internal sealed class PropertyKeyTree {

    abstract val children: Map<String, PropertyKeyTree>

    fun put(propertyKey: PropertyKey) {
        put(propertyKey, propertyKey.segments)
    }

    fun putAll(propertyKeys: Collection<PropertyKey>) {
        propertyKeys.forEach { put(it) }
    }

    protected abstract fun put(propertyKey: PropertyKey, segments: List<String>)

    internal data class InternalNode(val path: String? = null, override val children: MutableMap<String, PropertyKeyTree> = mutableMapOf()) :
            PropertyKeyTree() {

        override fun put(propertyKey: PropertyKey, segments: List<String>) {
            when {
                segments.size == 1 -> {
                    val segment = segments.single()
                    if (children.containsKey(segment)) {
                        throw IllegalStateException("property key ${propertyKey.value} must not have any children")
                    }
                    children[segment] = LeafNode(propertyKey)
                }
                segments.size > 1 -> {
                    val segment = segments.first()
                    val child = children.computeIfAbsent(segment) {
                        val childPath = path?.let { "$path${PropertyKey.PROPERTY_SEPARATOR}$segment" } ?: segment
                        InternalNode(childPath)
                    }
                    child.put(propertyKey, segments.drop(1))
                }
                else -> throw IllegalStateException("property key path must not be empty")
            }
        }

    }

    internal data class LeafNode(val propertyKey: PropertyKey) : PropertyKeyTree() {

        override val children: Map<String, PropertyKeyTree>
            get() = emptyMap()

        override fun put(propertyKey: PropertyKey, segments: List<String>) {
            throw UnsupportedOperationException("Cannot add property key ${propertyKey.value} as child of ${this.propertyKey.value}")
        }

    }

}