document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('horarioModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('horarioForm');
    const header = document.getElementById('horarioModalHeader');
    const titulo = document.getElementById('horarioModalTitulo');
    const id = document.getElementById('horarioId');
    const barbero = document.getElementById('horarioBarbero');
    const dia = document.getElementById('horarioDia');
    const inicio = document.getElementById('horarioInicio');
    const fin = document.getElementById('horarioFin');
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        if (!button) {
            return;
        }
        const modo = button.getAttribute('data-modo');

        form.reset();
        id.value = '';
        titulo.textContent = button.getAttribute('data-titulo') || '';
        header.classList.remove('bg-info', 'bg-warning', 'text-white');
        if (modo === 'editar') {
            id.value = button.getAttribute('data-id') || '';
            barbero.value = button.getAttribute('data-barbero') || '';
            dia.value = button.getAttribute('data-dia') || '';
            inicio.value = button.getAttribute('data-inicio') || '';
            fin.value = button.getAttribute('data-fin') || '';
            header.classList.add('bg-warning');
        } else {
            if (barbero.options.length > 0) {
                barbero.selectedIndex = 0;
            }
            header.classList.add('bg-info', 'text-white');
        }
    });
});

