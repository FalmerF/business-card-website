document.addEventListener('DOMContentLoaded', () => {
    const codeBlocks = document.querySelectorAll('.code-block');

    codeBlocks.forEach(setupCopyButtonsOfCodeBlock);
});

function setupCopyButtonsOfCodeBlock(codeBlock) {
    const codeBlockPre = codeBlock.querySelector('pre');
    const contentForCopy = codeBlockPre.textContent;

    const copyButtons = codeBlock.querySelectorAll('.copy-button');

    copyButtons.forEach(copyButton => setupCopyButton(copyButton, contentForCopy));
}

function setupCopyButton(copyButton, contentForCopy) {
    copyButton.addEventListener('click', async () => {
        console.log(navigator.clipboard);
        await navigator.clipboard.writeText(contentForCopy);

        copyButton.src = '/icons/check.svg';
        copyButton.style.pointerEvents = 'none'
        setTimeout(() => {
            copyButton.src = '/icons/copy.svg';
            copyButton.style.pointerEvents = 'unset'
        }, 2000);
    });
}