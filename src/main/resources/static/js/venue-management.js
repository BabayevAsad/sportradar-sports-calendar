document.addEventListener('DOMContentLoaded', () => {
    loadVenues();

    const venueForm = document.getElementById('venueForm');
    if (venueForm) {
        venueForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await handleVenueSubmit();
        });
    }
});

async function loadVenues() {
    try {
        const venues = await apiClient.get('/venues');
        renderVenueTable(venues);
    } catch (error) {
        console.error("Error loading venues:", error);
    }
}

function renderVenueTable(venues) {
    const tableBody = document.getElementById('venueListTable');
    if (!tableBody) return;

    tableBody.innerHTML = venues.map(venue => `
        <tr>
            <td>${venue.id}</td>
            <td>${venue.name}</td>
            <td>${venue.city}</td>
            <td>${venue.country}</td>
            <td class="text-end">
                <button class="btn btn-sm btn-outline-warning me-1" onclick="prepareEditVenue(${venue.id})">
                    <i class="fa-solid fa-pen"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" onclick="deleteVenue(${venue.id})">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

async function handleVenueSubmit() {
    const form = document.getElementById('venueForm');

    const venueData = {
        name: document.getElementById('venueName').value,
        city: document.getElementById('venueCity').value,
        country: document.getElementById('venueCountry').value
    };

    const editId = form.dataset.editId;

    try {
        if (editId) {
            await apiClient.put(`/venues/${editId}`, venueData);
        } else {
            await apiClient.post('/venues', venueData);
        }

        resetForm('venueForm');
        loadVenues();
    } catch (error) {
        console.error("Venue save failed", error);
    }
}

async function prepareEditVenue(id) {
    try {
        const venue = await apiClient.get(`/venues/${id}`);
        const form = document.getElementById('venueForm');

        document.getElementById('venueName').value = venue.name;
        document.getElementById('venueCity').value = venue.city;
        document.getElementById('venueCountry').value = venue.country;

        form.dataset.editId = id;
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.textContent = "Update Venue";
        submitBtn.classList.replace('btn-primary', 'btn-warning');
    } catch (error) {
        alert("Could not fetch venue details");
    }
}

async function deleteVenue(id) {
    if (confirm('Are you sure you want to delete this venue?')) {
        try {
            await apiClient.delete(`/venues/${id}`);
            loadVenues();
        } catch (error) {
            alert("Delete failed");
        }
    }
}