export const showCustomAlert = (message) => {
    const customAlert = document.createElement('div');
    customAlert.classList.add('custom-alert');
    customAlert.textContent = message;

    document.body.appendChild(customAlert);

    setTimeout(() => {
        customAlert.remove();
    }, 1500);
}