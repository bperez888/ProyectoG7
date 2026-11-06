document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('servicioModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('servicioForm');
    const header = document.getElementById('servicioModalHeader');
    const titulo = document.getElementById('servicioModalTitulo');
    const id = document.getElementById('servicioId');
    const nombre = document.getElementById('servicioNombre');
    const descripcion = document.getElementById('servicioDescripcion');
    const precio = document.getElementById('servicioPrecio');
    const duracion = document.getElementById('servicioDuracion');
    const activo = document.getElementById('servicioActivo');
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
            nombre.value = button.getAttribute('data-nombre') || '';
            descripcion.value = button.getAttribute('data-descripcion') || '';
            precio.value = button.getAttribute('data-precio') || '';
            duracion.value = button.getAttribute('data-duracion') || '';
            activo.checked = button.getAttribute('data-activo') === 'true';
            header.classList.add('bg-warning');
        } else {

            activo.checked = true;
            header.classList.add('bg-info', 'text-white');
        }
    });
});

