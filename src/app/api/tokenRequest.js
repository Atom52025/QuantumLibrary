export const dynamic = 'force-dynamic'; // defaults to auto

const INTERNAL_API_URL = process.env.NEXT_PUBLIC_INTERNAL_API_URL;
const EXTERNAL_API_URL = process.env.NEXT_PUBLIC_EXTERNAL_API_URL;

// Detects if its executing on client or in server
const API_URL = typeof window === 'undefined' ? INTERNAL_API_URL : EXTERNAL_API_URL;

export async function GET(url, token) {
  // Create Query String
  const completeUrl = `${API_URL}/${url}`;

  // Fetch Data
  const res = await fetch(`${API_URL}/${url}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (!res.ok) {
    console.log('Failed to fetch data');
    return null;
  }
  return res.json();
}

export async function POST(url, token, body) {
  // Create Query String
  const completeUrl = `${API_URL}/${url}`;

  // Fetch Data
  const res = await fetch(completeUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(body),
  });
  if (!res.ok) {
    console.log('Failed to fetch data');
    throw new Error(`Failed to fetch data: ${res.status}`);
  }
  return res?.json();
}

export async function DELETE(url, token) {
  // Create Query String
  const completeUrl = `${API_URL}/${url}`;

  // Fetch Data
  const res = await fetch(completeUrl, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (!(res.ok || res.status === 204)) {
    console.log('Entity not found');
  }
}

export async function PATCH(url, token, body) {
  // Create Query String
  const completeUrl = `${API_URL}/${url}`;

  // Fetch Data
  const res = await fetch(completeUrl, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: body ? JSON.stringify(body) : null,
  });
  if (!(res.ok || res.status === 204)) {
    console.log('Failed to fetch data');
    throw new Error(`Failed to fetch data: ${res.status}`);
  }
  return res.status === 204 ? null : res.json();
}
