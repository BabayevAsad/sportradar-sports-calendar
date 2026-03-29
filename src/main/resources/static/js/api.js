const API_BASE_URL = 'http://localhost:8080/rest/api';

const apiClient = {
    async get(endpoint) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`);
            if (!response.ok) throw new Error(`Fetch error: ${response.statusText}`);
            return await response.json();
        } catch (error) {
            console.error("API Get Error:", error);
            throw error;
        }
    },

   async post(endpoint, data) {
       const response = await fetch(`${API_BASE_URL}${endpoint}`, {
           method: 'POST',
           headers: { 'Content-Type': 'application/json' },
           body: JSON.stringify(data)
       });

       const responseData = await response.json().catch(() => null);

       if (!response.ok) {
           throw responseData;
       }

       return responseData;
   },

   async put(endpoint, data) {
       const response = await fetch(`${API_BASE_URL}${endpoint}`, {
           method: 'PUT',
           headers: { 'Content-Type': 'application/json' },
           body: JSON.stringify(data)
       });

       const responseData = await response.json().catch(() => null);

       if (!response.ok) {
           throw responseData;
       }

       return responseData;
   },

    async delete(endpoint) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'DELETE'
        });
        if (!response.ok) throw new Error(`Delete error: ${response.statusText}`);
        return true;
    }
};