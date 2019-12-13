module.exports = {
    root: true,
    parserOptions: {
        parser: "babel-eslint",
        sourceType: "module"
    },
    env: {
        browser: true,
        node: true,
        es6: true
    },
    extends: ["plugin:vue/recommended", "eslint:recommended"],

    // "off" or 0 - 关闭规则
    // "warn" or 1 - 将规则视为一个警告（不会影响退出码）
    // "error" or 2 - 将规则视为一个错误 (退出码为1)
    // add your custom rules here
    //it is base on https://github.com/vuejs/eslint-config-vue
    rules: {
        "generator-star-spacing": "off",
        "no-console": "off",
        "no-const-assign": "error",
        "no-debugger": process.env.NODE_ENV === "production" ? 2 : "off",
        "no-dupe-args": "error",
        "no-duplicate-case": "error",
        "no-empty": ["error", { allowEmptyCatch: true }],
        "no-mixed-operators": "off",
        "no-prototype-builtins": "warn",
        "no-redeclare": "warn",
        "no-self-assign": "error",
        "no-self-compare": "error",
        "no-shadow-restricted-names": "warn",
        "no-unused-vars": "warn",
        // "prefer-const": ["error", { ignoreReadBeforeAssign: false }],
        "semi-spacing": ["error", { before: false, after: true }],
        "spaced-comment": ["error", "always", { markers: ["global", "globals", "eslint", "eslint-disable", "*package", "!", ","] }],
        "vue/attribute-hyphenation": "off",
        "vue/component-name-in-template-casing": "off",
        "vue/html-closing-bracket-newline": "off",
        "vue/html-closing-bracket-spacing": "off",
        "vue/html-indent": "off",
        "vue/html-self-closing": "off",
        "vue/max-attributes-per-line": ["error", { singleline: 5, multiline: { max: 1, allowFirstLine: false } }],
        "vue/multiline-html-element-content-newline": "off",
        "vue/name-property-casing": ["error", "PascalCase"],
        "vue/no-parsing-error": "off",
        "vue/no-unused-components": "off",
        "vue/no-use-v-if-with-v-for": "off",
        "vue/order-in-components": "off",
        "vue/singleline-html-element-content-newline": "off",
        indent: ["error", 4, { SwitchCase: 1 }]
    }
};
