/**
 * Initialise log4js first, so we don't miss any log messages
 */
var config = {
    appenders: {
        out: { type: 'console' },
        everything: {
            type: 'file',
            layout: { type: 'pattern', pattern: '[%d] [%p] [%c] %m' },
            filename: global.configlog.pathapp, // (global.configlog.so == global.constants.WINDOWS) ? global.configlog.windowsppathapp : global.configlog.linuxpathapp,
            maxLogSize: 10485760,
            backups: 10
        },
        emergencies: {
            type: 'file',
            layout: { type: 'pattern', pattern: '[%d] [%p] [%c] %m' },
            filename: global.configlog.patherror, //(global.configlog.so == global.constants.WINDOWS) ? global.configlog.windowspatherr : global.configlog.linuxpatherr,
            maxLogSize: 10485760,
            numBackups: 5
        },
        errors: {
            type: 'logLevelFilter',
            appender: 'emergencies',
            level: 'error'
        }
    },
    categories: {
        default: { appenders: ['errors', 'everything'], level: 'all' }
    }
}

module.exports = config;