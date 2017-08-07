/**
 * Created by EvanKing on 8/7/17.
 */

function submit() {
    document.getElementById('resource-form').submit()
}

function helpLink() {
    location.href='https://simplemde.com/markdown-guide';
}

var simplemde = new SimpleMDE({
    toolbar: [
        {
            name: "bold",
            action: SimpleMDE.toggleBold,
            className: "fa fa-bold",
            title: "Bold",
        },
        {
            name: "italic",
            action: SimpleMDE.toggleItalic,
            className: "fa fa-italic",
            title: "Italic",
        },
        {
            name: "strikethrough",
            action: SimpleMDE.toggleStrikethrough,
            className: "fa fa-strikethrough",
            title: "Strikethrough",
        },
        {
            name: "heading",
            action: SimpleMDE.toggleHeadingSmaller,
            className: "fa fa-header",
            title: "Heading",
        },
        "|", // Separator

        {
            name: "code",
            action: SimpleMDE.toggleCodeBlock,
            className: "fa fa-code",
            title: "Code",
        },
        {
            name: "clean-block	",
            action: SimpleMDE.cleanBlock,
            className: "fa fa-eraser fa-clean-block",
            title: "Clean block",
        },
        "|", // Separator
        {
            name: "quote",
            action: SimpleMDE.toggleBlockquote,
            className: "fa fa-quote-left",
            title: "Quote",
        },
        {
            name: "unordered-list",
            action: SimpleMDE.toggleUnorderedList,
            className: "fa fa-list-ul",
            title: "Generic List",
        },
        {
            name: "ordered-list",
            action: SimpleMDE.toggleOrderedList,
            className: "fa fa-list-ol",
            title: "Numbered List",
        },

        "|", // Separator
        {
            name: "link",
            action: SimpleMDE.drawLink,
            className: "fa fa-link",
            title: "Create Link",
        },
        {
            name: "image",
            action: SimpleMDE.drawImage,
            className: "fa fa-picture-o",
            title: "Insert Image",
        },
        "|", // Separator
        {
            name: "preview",
            action: SimpleMDE.togglePreview,
            className: "fa fa-eye no-disablee",
            title: "Toggle Preview",
        },
        {
            name: "side-by-side",
            action: SimpleMDE.toggleSideBySide,
            className: "fa fa-columns no-disable no-mobile",
            title: "Toggle Side by Side",
        },
        {
            name: "fullscreen",
            action: SimpleMDE.toggleFullScreen,
            className: "fa fa-arrows-alt no-disable no-mobile",
            title: "Toggle Fullscreen",
        },
        "|", // Separator
        {
            name: "guide",
            action: helpLink,
            className: "fa fa-question-circle",
            title: "Markdown Guide",
        },
        "|", // Separator
        {
            name: "submit",
            action: submit,
            className: "fa fa-save",
            title: "Submit",
        }
    ],
});

