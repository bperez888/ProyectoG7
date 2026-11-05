document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('barberoModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('barberoForm');
    const header = document.getElementById('barberoModalHeader');
    const titulo = document.getElementById('barberoModalTitulo');
    const id = document.getElementById('barberoId');
    const nombre = document.getElementById('barberoNombre');
    const telefono = document.getElementById('barberoTelefono');
    const foto = document.getElementById('barberoFoto');
    const activo = document.getElementById('barberoActivo');
    const preview = document.getElementById('barberoPreview');
    const servicios = document.querySelectorAll('.servicio-check');

    function mostrarPreview(ruta) {
        if (ruta) {
            preview.src = ruta;
            preview.classList.remove('d-none');
        } else {
            preview.removeAttribute('src');
            preview.classList.add('d-none');
        }
    }

    foto.addEventListener('change', function () {
        mostrarPreview(this.value);
    });

    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        if (!button) {
            return;
        }
        const modo = button.getAttribute('data-modo');

        form.reset();
        id.value = '';
        mostrarPreview('');
        titulo.textContent = button.getAttribute('data-titulo') || '';
        header.classList.remove('bg-info', 'bg-warning', 'text-white');
        if (modo === 'editar') {
            id.value = button.getAttribute('data-id') || '';
            nombre.value = button.getAttribute('data-nombre') || '';
            telefono.value = button.getAttribute('data-telefono') || '';
            foto.value = button.getAttribute('data-foto') || '';
            activo.checked = button.getAttribute('data-activo') === 'true';
            const idsSeleccionados = button.querySelectorAll('.servicio-id');
            for (let i = 0; i < servicios.length; i++) {
                servicios[i].checked = false;
                for (let j = 0; j < idsSeleccionados.length; j++) {
                    if (servicios[i].value === idsSeleccionados[j].textContent) {
                        servicios[i].checked = true;
                    }
                }
            }
            mostrarPreview(foto.value);
            header.classList.add('bg-warning');
        } else {

            activo.checked = true;
            header.classList.add('bg-info', 'text-white');
        }
    });
});


