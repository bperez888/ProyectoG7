document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('clienteModal');
    if (!modal) {
        return;
    }
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        if (!button) {
            return;
        }
        document.getElementById('clienteNombre').textContent = button.getAttribute('data-nombre') || '';
        document.getElementById('clienteCorreo').textContent = button.getAttribute('data-correo') || '';
        document.getElementById('clienteTelefono').textContent = button.getAttribute('data-telefono') || '-';
    });
});
