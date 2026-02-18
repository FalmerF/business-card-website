document.addEventListener('DOMContentLoaded', () => {
    const tabComponents = document.querySelectorAll('.tab');
    tabComponents.forEach(setupTabComponent);
});

function setupTabComponent(tabComponent) {
    tabComponent.dataset.index = '0';

    const tabContent = tabComponent.querySelector('.tab-content');
    const tabElements = Array.from(tabContent.children);

    const buttonHolder = tabComponent.querySelector('.tab-button-holder');
    const buttons = buttonHolder.querySelectorAll('.tab-button');

    buttons.forEach((button, index) => {
        if (index == 0) {
           button.classList.add('selected');
        }

        button.addEventListener('click', () => {
            const prevButtonIndex = tabComponent.dataset.index;
            const prevButton = buttons[prevButtonIndex];
            prevButton.classList.remove('selected');

            tabComponent.dataset.index = index;

            button.classList.add('selected');
            tabContent.style.setProperty('--tab-index', index);
        })
    })
}