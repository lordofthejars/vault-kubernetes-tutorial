/**
 * Extends the AsciiDoc syntax by adding support for including the output of shell commands.
 *
 * This include directive executes given shell script and includes its output in the document.
 * The invoked command has to be in the $PATH.
 *
 * Usage:
 *
 *  include::cmd:ls[flags='-al',block=true,print=true]
 *
 *  - `flags` will be added to command which is executes, e.g. include::cmd:ls[flags='-al']
 *  - `block`, when set to true will wrap the output in [source,bash] block
 *  - `print`, when set to true will add $ command before the output
 *
 * @author Bartosz Majsak <bartosz.majsak@gmail.com>
 */

const spawnSync = require('child_process').spawnSync

const shellOptions = {
    shell: true,
    cwd: process.cwd(),
    env: {
        PATH: process.env.PATH
    }
}

function includeShellCommand() {
    const self = this

    // This is important as it pushes this processor to be first handling include:: directive.
    // Without this, the default one would try to include this reference
    // resulting in failure: `Unresolved include directive in [...] - include:cmd://ls[flags='-al']`
    self.$option('position', '>>')

    self.handles((target) => target.startsWith('cmd:'))

    self.process((doc, reader, target, attrs) => {
        const command = target.substring(4) + ' ' + attrs['flags']
        const result = spawnSync("bash -c '" + command + "'", shellOptions)
        let output = result.output[1].toString() + result.output[2].toString()

        if (!!attrs['block']) {
            id = "cmd-" + Math.floor((Math.random() * 1000) + 1)
            output = `[source,bash]
----
${!!attrs['print'] ? '$ ' + command : ''}
${output}
----`
        }

        reader.pushInclude(output, target, target, 1, attrs)
    })
}

function register(registry, context) {
    registry.includeProcessor(includeShellCommand)
}

module.exports.register = register