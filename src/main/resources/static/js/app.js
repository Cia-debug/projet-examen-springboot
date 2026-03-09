const API = '';

// ===== Utility Functions =====

async function fetchJSON(url, options = {}) {
    try {
        const res = await fetch(API + url, {
            headers: { 'Content-Type': 'application/json' },
            ...options
        });
        if (!res.ok) {
            const text = await res.text();
            throw new Error(text || res.statusText);
        }
        if (res.status === 204) return null;
        return await res.json();
    } catch (err) {
        showToast(err.message, 'error');
        throw err;
    }
}

function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    const icons = { success: '✅', error: '❌', info: 'ℹ️' };
    toast.innerHTML = `<span>${icons[type] || ''}</span><span>${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

// ===== Navigation =====

function navigateTo(page) {
    document.querySelectorAll('.page-section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));

    const section = document.getElementById(`page-${page}`);
    const navItem = document.querySelector(`.nav-item[data-page="${page}"]`);
    if (section) section.classList.add('active');
    if (navItem) navItem.classList.add('active');

    const loaders = {
        'dashboard': loadDashboard,
        'candidats': loadCandidats,
        'correcteurs': loadCorrecteurs,
        'matieres': loadMatieres,
        'notes': loadNotes,
        'calcul': loadCalcul
    };
    if (loaders[page]) loaders[page]();
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', () => navigateTo(item.dataset.page));
    });
    navigateTo('dashboard');
});

// ===== Dashboard =====

async function loadDashboard() {
    try {
        const [candidats, correcteurs, matieres, notes] = await Promise.all([
            fetchJSON('/candidats'),
            fetchJSON('/correcteurs'),
            fetchJSON('/matieres'),
            fetchJSON('/notes')
        ]);
        document.getElementById('stat-candidats').textContent = candidats.length;
        document.getElementById('stat-correcteurs').textContent = correcteurs.length;
        document.getElementById('stat-matieres').textContent = matieres.length;
        document.getElementById('stat-notes').textContent = notes.length;
    } catch (e) { /* toast already shown */ }
}

// ===== Candidats =====

async function loadCandidats() {
    try {
        const data = await fetchJSON('/candidats');
        const tbody = document.getElementById('candidats-tbody');
        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" class="empty-state"><p>Aucun candidat</p></td></tr>';
            return;
        }
        tbody.innerHTML = data.map(c => `
            <tr>
                <td><span class="badge badge-blue">#${c.id}</span></td>
                <td>${c.nom}</td>
                <td>${c.numero}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-outline btn-icon btn-sm" onclick="editCandidat(${c.id},'${c.nom}','${c.numero}')">✏️</button>
                        <button class="btn btn-danger btn-icon btn-sm" onclick="deleteCandidat(${c.id})">🗑️</button>
                    </div>
                </td>
            </tr>
        `).join('');
    } catch (e) {}
}

function showCandidatModal(id = null, nom = '', numero = '') {
    document.getElementById('candidat-modal-title').textContent = id ? 'Modifier Candidat' : 'Nouveau Candidat';
    document.getElementById('candidat-id').value = id || '';
    document.getElementById('candidat-nom').value = nom;
    document.getElementById('candidat-numero').value = numero;
    document.getElementById('candidat-modal').classList.add('show');
}

function closeCandidatModal() {
    document.getElementById('candidat-modal').classList.remove('show');
}

function editCandidat(id, nom, numero) {
    showCandidatModal(id, nom, numero);
}

async function saveCandidat() {
    const id = document.getElementById('candidat-id').value;
    const body = {
        nom: document.getElementById('candidat-nom').value,
        numero: document.getElementById('candidat-numero').value
    };
    try {
        if (id) {
            await fetchJSON(`/candidats/${id}`, { method: 'PUT', body: JSON.stringify(body) });
            showToast('Candidat modifié', 'success');
        } else {
            await fetchJSON('/candidats', { method: 'POST', body: JSON.stringify(body) });
            showToast('Candidat créé', 'success');
        }
        closeCandidatModal();
        loadCandidats();
        loadDashboard();
    } catch (e) {}
}

async function deleteCandidat(id) {
    if (!confirm('Supprimer ce candidat ?')) return;
    try {
        await fetchJSON(`/candidats/${id}`, { method: 'DELETE' });
        showToast('Candidat supprimé', 'success');
        loadCandidats();
        loadDashboard();
    } catch (e) {}
}

// ===== Correcteurs =====

async function loadCorrecteurs() {
    try {
        const data = await fetchJSON('/correcteurs');
        const tbody = document.getElementById('correcteurs-tbody');
        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="3" class="empty-state"><p>Aucun correcteur</p></td></tr>';
            return;
        }
        tbody.innerHTML = data.map(c => `
            <tr>
                <td><span class="badge badge-purple">#${c.id}</span></td>
                <td>${c.nom}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-outline btn-icon btn-sm" onclick="editCorrecteur(${c.id},'${c.nom}')">✏️</button>
                        <button class="btn btn-danger btn-icon btn-sm" onclick="deleteCorrecteur(${c.id})">🗑️</button>
                    </div>
                </td>
            </tr>
        `).join('');
    } catch (e) {}
}

function showCorrecteurModal(id = null, nom = '') {
    document.getElementById('correcteur-modal-title').textContent = id ? 'Modifier Correcteur' : 'Nouveau Correcteur';
    document.getElementById('correcteur-id').value = id || '';
    document.getElementById('correcteur-nom').value = nom;
    document.getElementById('correcteur-modal').classList.add('show');
}

function closeCorrecteurModal() {
    document.getElementById('correcteur-modal').classList.remove('show');
}

function editCorrecteur(id, nom) {
    showCorrecteurModal(id, nom);
}

async function saveCorrecteur() {
    const id = document.getElementById('correcteur-id').value;
    const body = { nom: document.getElementById('correcteur-nom').value };
    try {
        if (id) {
            await fetchJSON(`/correcteurs/${id}`, { method: 'PUT', body: JSON.stringify(body) });
            showToast('Correcteur modifié', 'success');
        } else {
            await fetchJSON('/correcteurs', { method: 'POST', body: JSON.stringify(body) });
            showToast('Correcteur créé', 'success');
        }
        closeCorrecteurModal();
        loadCorrecteurs();
        loadDashboard();
    } catch (e) {}
}

async function deleteCorrecteur(id) {
    if (!confirm('Supprimer ce correcteur ?')) return;
    try {
        await fetchJSON(`/correcteurs/${id}`, { method: 'DELETE' });
        showToast('Correcteur supprimé', 'success');
        loadCorrecteurs();
        loadDashboard();
    } catch (e) {}
}

// ===== Matières =====

async function loadMatieres() {
    try {
        const data = await fetchJSON('/matieres');
        const tbody = document.getElementById('matieres-tbody');
        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="3" class="empty-state"><p>Aucune matière</p></td></tr>';
            return;
        }
        tbody.innerHTML = data.map(m => `
            <tr>
                <td><span class="badge badge-green">#${m.id}</span></td>
                <td>${m.nom}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-outline btn-icon btn-sm" onclick="editMatiere(${m.id},'${m.nom}')">✏️</button>
                        <button class="btn btn-danger btn-icon btn-sm" onclick="deleteMatiere(${m.id})">🗑️</button>
                    </div>
                </td>
            </tr>
        `).join('');
    } catch (e) {}
}

function showMatiereModal(id = null, nom = '') {
    document.getElementById('matiere-modal-title').textContent = id ? 'Modifier Matière' : 'Nouvelle Matière';
    document.getElementById('matiere-id').value = id || '';
    document.getElementById('matiere-nom').value = nom;
    document.getElementById('matiere-modal').classList.add('show');
}

function closeMatiereModal() {
    document.getElementById('matiere-modal').classList.remove('show');
}

function editMatiere(id, nom) {
    showMatiereModal(id, nom);
}

async function saveMatiere() {
    const id = document.getElementById('matiere-id').value;
    const body = { nom: document.getElementById('matiere-nom').value };
    try {
        if (id) {
            await fetchJSON(`/matieres/${id}`, { method: 'PUT', body: JSON.stringify(body) });
            showToast('Matière modifiée', 'success');
        } else {
            await fetchJSON('/matieres', { method: 'POST', body: JSON.stringify(body) });
            showToast('Matière créée', 'success');
        }
        closeMatiereModal();
        loadMatieres();
        loadDashboard();
    } catch (e) {}
}

async function deleteMatiere(id) {
    if (!confirm('Supprimer cette matière ?')) return;
    try {
        await fetchJSON(`/matieres/${id}`, { method: 'DELETE' });
        showToast('Matière supprimée', 'success');
        loadMatieres();
        loadDashboard();
    } catch (e) {}
}

// ===== Notes =====

async function loadNotes() {
    try {
        const data = await fetchJSON('/notes');
        const tbody = document.getElementById('notes-tbody');
        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="empty-state"><p>Aucune note</p></td></tr>';
            return;
        }
        tbody.innerHTML = data.map(n => `
            <tr>
                <td><span class="badge badge-blue">#${n.id}</span></td>
                <td>${n.candidat.nom}</td>
                <td>${n.correcteur.nom}</td>
                <td>${n.matiere.nom}</td>
                <td><strong>${n.note}</strong></td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-danger btn-icon btn-sm" onclick="deleteNote(${n.id})">🗑️</button>
                    </div>
                </td>
            </tr>
        `).join('');

        // Also load selects for the note form
        const [candidats, correcteurs, matieres] = await Promise.all([
            fetchJSON('/candidats'),
            fetchJSON('/correcteurs'),
            fetchJSON('/matieres')
        ]);
        const selCandidat = document.getElementById('note-candidat');
        const selCorrecteur = document.getElementById('note-correcteur');
        const selMatiere = document.getElementById('note-matiere');

        selCandidat.innerHTML = '<option value="">-- Candidat --</option>' +
            candidats.map(c => `<option value="${c.id}">${c.nom} (${c.numero})</option>`).join('');
        selCorrecteur.innerHTML = '<option value="">-- Correcteur --</option>' +
            correcteurs.map(c => `<option value="${c.id}">${c.nom}</option>`).join('');
        selMatiere.innerHTML = '<option value="">-- Matière --</option>' +
            matieres.map(m => `<option value="${m.id}">${m.nom}</option>`).join('');
    } catch (e) {}
}

function showNoteModal() {
    document.getElementById('note-value').value = '';
    document.getElementById('note-modal').classList.add('show');
}

function closeNoteModal() {
    document.getElementById('note-modal').classList.remove('show');
}

async function saveNote() {
    const body = {
        idCandidat: parseInt(document.getElementById('note-candidat').value),
        idCorrecteur: parseInt(document.getElementById('note-correcteur').value),
        idMatiere: parseInt(document.getElementById('note-matiere').value),
        note: parseFloat(document.getElementById('note-value').value)
    };
    try {
        await fetchJSON('/notes', { method: 'POST', body: JSON.stringify(body) });
        showToast('Note ajoutée', 'success');
        closeNoteModal();
        loadNotes();
        loadDashboard();
    } catch (e) {}
}

async function deleteNote(id) {
    if (!confirm('Supprimer cette note ?')) return;
    try {
        await fetchJSON(`/notes/${id}`, { method: 'DELETE' });
        showToast('Note supprimée', 'success');
        loadNotes();
        loadDashboard();
    } catch (e) {}
}

// ===== Calcul Note Finale =====

async function loadCalcul() {
    try {
        const [candidats, matieres] = await Promise.all([
            fetchJSON('/candidats'),
            fetchJSON('/matieres')
        ]);
        const selCandidat = document.getElementById('calcul-candidat');
        const selMatiere = document.getElementById('calcul-matiere');

        selCandidat.innerHTML = '<option value="">-- Candidat --</option>' +
            candidats.map(c => `<option value="${c.id}">${c.nom} (${c.numero})</option>`).join('');
        selMatiere.innerHTML = '<option value="">-- Matière --</option>' +
            matieres.map(m => `<option value="${m.id}">${m.nom}</option>`).join('');

        document.getElementById('calcul-result').style.display = 'none';
    } catch (e) {}
}

async function calculerNoteFinale() {
    const idCandidat = document.getElementById('calcul-candidat').value;
    const idMatiere = document.getElementById('calcul-matiere').value;

    if (!idCandidat || !idMatiere) {
        showToast('Sélectionnez un candidat et une matière', 'error');
        return;
    }

    const btn = document.getElementById('btn-calculer');
    btn.disabled = true;
    btn.innerHTML = '<span class="loading-spinner"></span> Calcul en cours...';

    try {
        const result = await fetchJSON(`/calcul-note-finale/${idCandidat}/${idMatiere}`, { method: 'POST' });

        document.getElementById('result-note').textContent = result.noteFinale;
        document.getElementById('result-candidat').textContent = result.nomCandidat;
        document.getElementById('result-matiere').textContent = result.nomMatiere;
        document.getElementById('calcul-result').style.display = 'block';

        showToast('Note finale calculée !', 'success');
    } catch (e) {}

    btn.disabled = false;
    btn.innerHTML = '🧮 Calculer la Note Finale';
}

async function voirNoteFinale() {
    const idCandidat = document.getElementById('calcul-candidat').value;
    const idMatiere = document.getElementById('calcul-matiere').value;

    if (!idCandidat || !idMatiere) {
        showToast('Sélectionnez un candidat et une matière', 'error');
        return;
    }

    try {
        const result = await fetchJSON(`/note-finale/${idCandidat}/${idMatiere}`);
        document.getElementById('result-note').textContent = result.noteFinale;
        document.getElementById('result-candidat').textContent = result.nomCandidat;
        document.getElementById('result-matiere').textContent = result.nomMatiere;
        document.getElementById('calcul-result').style.display = 'block';
    } catch (e) {}
}
