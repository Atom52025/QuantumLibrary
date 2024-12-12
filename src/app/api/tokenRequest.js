export const dynamic = 'force-dynamic'; // defaults to auto

const INTERNAL_API_URL = process.env.INTERNAL_API_URL;
const EXTERNAL_API_URL = process.env.NEXT_PUBLIC_API_URL;

export async function GET(url, token, isExternal = false) {
  // Create Query String
  const completeUrl = isExternal ? `${EXTERNAL_API_URL}/${url}` : `${INTERNAL_API_URL}/${url}`;

  // Fetch Data
  const res = await fetch(completeUrl, {
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

export async function POST(url, token, body, isExternal = false) {
  // Create Query String
  const completeUrl = isExternal ? `${EXTERNAL_API_URL}/${url}` : `${INTERNAL_API_URL}/${url}`;

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

export async function DELETE(url, token, isExternal = false) {
  // Create Query String
  const completeUrl = isExternal ? `${EXTERNAL_API_URL}/${url}` : `${INTERNAL_API_URL}/${url}`;

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

export async function PATCH(url, token, body, isExternal = false) {
  // Create Query String
  const completeUrl = isExternal ? `${EXTERNAL_API_URL}/${url}` : `${INTERNAL_API_URL}/${url}`;

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
