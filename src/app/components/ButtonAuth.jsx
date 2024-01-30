'use client';

import { signIn, signOut, useSession } from 'next-auth/react';

import { Button } from '@nextui-org/button';
import { getToken } from 'next-auth/jwt';

export default function ButtonAuth() {
  const { data: session, status } = useSession();

  console.log({ session, status });

  if (status === 'loading') {
    return <p>Loading...</p>;
  }

  if (session) {
    return (
      <>
        Signed in as {session.user?.email} <br />
        Signed in as {session.accessToken} <br />
        <Button onClick={() => signOut()} className="btn btn-danger">
          Sign out
        </Button>
      </>
    );
  }
  return (
    <>
      Not signed in <br />
      <Button onClick={() => signIn()} className="btn btn-primary">
        Sign in
      </Button>
    </>
  );
}
