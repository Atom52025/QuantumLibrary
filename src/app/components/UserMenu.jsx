'use client';

import { Input, Popover, PopoverContent, PopoverTrigger, User } from '@nextui-org/react';
import { signIn, signOut, useSession } from 'next-auth/react';

export default function UserMenu() {
  // Get Session
  const { data: session, status } = useSession();

  const notAuthenticated = () => {
    return status === 'loading' ? <p>Loading...</p> : <Input type="button" className="w-1/12 border border-2 border-gray-500 rounded-xl" value="Sign In" onClick={() => signIn()} />;
  };

  return (
    <>
      {status === 'authenticated' ? (
        <Popover showArrow placement="bottom">
          <PopoverTrigger>
            <User
              name={session.user.username}
              avatarProps={{
                src: 'https://api.dicebear.com/7.x/identicon/svg?seed=' + session.user.username,
              }}
            />
          </PopoverTrigger>
          <PopoverContent className="p-1">
            <Input type="button" value="Sign Out" onClick={() => signOut()} />
          </PopoverContent>
        </Popover>
      ) : (
        notAuthenticated()
      )}
    </>
  );
}
