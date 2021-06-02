package net.cjsah.bot.console.command.context

import net.cjsah.bot.console.command.Command
import net.cjsah.bot.console.command.Dispatcher
import net.cjsah.bot.console.command.tree.CommandNode

class CommandContextBuilder<S>(
    private val dispatcher: Dispatcher<S>,
    private var source: S,
    private val rootNode: CommandNode<S>,
    start: Int
) {
    private val arguments = LinkedHashMap<String, ParsedResult<*>>()
    private val nodes = ArrayList<ParsedNode<S>>()
    private var command: Command<S>? = null
    private var child: CommandContextBuilder<S>? = null
    private var range: IntRange = IntRange(start, start)

    fun withSource(source: S): CommandContextBuilder<S> {
        this.source = source
        return this
    }

    fun withArgument(name: String, argument: ParsedResult<*>): CommandContextBuilder<S> {
        arguments[name] = argument
        return this
    }

    fun withCommand(command: Command<S>): CommandContextBuilder<S> {
        this.command = command
        return this
    }

    fun withNode(node: CommandNode<S>, range: IntRange): CommandContextBuilder<S> {
        nodes.add(ParsedNode(node, range))
        this.range = StringRange.encompassing(this.range, range)
        modifier = node.getRedirectModifier()
        forks = node.isFork()
        return this
    }

    fun copy(): CommandContextBuilder<S>? {
        val copy: CommandContextBuilder<S> = CommandContextBuilder(dispatcher, source, rootNode, range.getStart())
        copy.command = command
        copy.arguments.putAll(arguments)
        copy.nodes.addAll(nodes)
        copy.child = child
        copy.range = range
        copy.forks = forks
        return copy
    }

    fun withChild(child: CommandContextBuilder<S>?): CommandContextBuilder<S>? {
        this.child = child
        return this
    }

    fun getLastChild(): CommandContextBuilder<S>? {
        var result: CommandContextBuilder<S>? = this
        while (result!!.getChild() != null) {
            result = result.getChild()
        }
        return result
    }

    fun build(input: String?): CommandContext<S>? {
        return CommandContext(
            source,
            input,
            arguments,
            command,
            rootNode,
            nodes,
            range,
            if (child == null) null else child!!.build(input),
            modifier,
            forks
        )
    }

    fun findSuggestionContext(cursor: Int): SuggestionContext<S>? {
        if (range.getStart() <= cursor) {
            return if (range.getEnd() < cursor) {
                if (child != null) {
                    child!!.findSuggestionContext(cursor)
                } else if (!nodes.isEmpty()) {
                    val last: ParsedCommandNode<S> = nodes[nodes.size - 1]
                    SuggestionContext(last.getNode(), last.getRange().getEnd() + 1)
                } else {
                    SuggestionContext(rootNode, range.getStart())
                }
            } else {
                var prev: CommandNode<S>? = rootNode
                for (node in nodes) {
                    val nodeRange: StringRange = node.getRange()
                    if (nodeRange.getStart() <= cursor && cursor <= nodeRange.getEnd()) {
                        return SuggestionContext(prev, nodeRange.getStart())
                    }
                    prev = node.getNode()
                }
                checkNotNull(prev) { "Can't find node before cursor" }
                SuggestionContext(prev, range.getStart())
            }
        }
        throw IllegalStateException("Can't find node before cursor")
    }

    fun getSource() = source

    fun getRootNode() = rootNode

    fun getArguments() = arguments

    fun getChild() = child

    fun getCommand() = command

    fun getNodes() = nodes

    fun getDispatcher() = dispatcher

    fun getRange() = range

}