'use client';

import { Avatar, AvatarGroup, Button, Divider } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useState } from 'react';
import { FaAngleLeft, FaAngleRight } from 'react-icons/fa';

import Link from 'next/link';

import { DELETE, PATCH } from '@/app/api/tokenRequest';
import GroupCreateModal from '@/app/components/modals/GroupCreateModal';
import { motion } from 'framer-motion';

export default function GroupListSection({ gData, open, setOpen }) {
  const { data: session } = useSession({ required: true });
  const [groups, setGroups] = useState(gData);

  const acceptGroup = async (group) => {
    const url = 'api/user/group/' + group.id;
    await PATCH(url, session.user.token, null);
    const updatedGroup = {
      ...group,
      accepted: true,
    };
    setGroups((prevGroups) => ({
      ...prevGroups,
      pending: prevGroups.pending.filter((userGroup) => userGroup.id !== group.id),
      accepted: [...prevGroups.accepted, updatedGroup],
    }));
  };

  const declineGroup = async (group) => {
    const url = 'api/user/group/' + group.id;
    await DELETE(url, session.user.token);
    setGroups((prevGroups) => ({
      ...prevGroups,
      pending: prevGroups.pending.filter((userGroup) => userGroup.id !== group.id),
    }));
  };

  return (
    <div className={`h-full w-[200px] lg:bg-gray-800/50 bg-gray-800 shadow-inner flex flex-col top-0 z-40 absolute ${open ? 'right-0' : 'right-[-200px]'}`}>
      <div className="p-3 flex-grow w-full">
        <p className="text-2xl font-bold"> Grupos </p>
        {groups.accepted.map((group) => {
          return (
            <Link href={'/group/' + group.id + '/games'} key={group.id + 'a'}>
              <Divider className="my-2" />
              <p className="text-xl"> {group.name} </p>
              <AvatarGroup isBordered className="flex justify-start my-2">
                {group.users?.map((userGroup) => (
                  <Avatar key={userGroup.user.username + group.id.toString()} src={userGroup.user.image} />
                ))}
              </AvatarGroup>
            </Link>
          );
        })}
        {groups.pending.length > 0 && (
          <>
            <p className="text-2xl font-bold mt-5 "> Invitaciones </p>
            {groups.pending.map((group) => {
              return (
                <div key={group.id + 'p'}>
                  <Divider className="my-2" />
                  <p className="text-xl"> {group.name} </p>
                  <AvatarGroup isBordered className="flex justify-start my-2">
                    {group.users?.map((userGroup) => (
                      <Avatar key={userGroup.user.username + group.id.toString()} src={userGroup.user.image} />
                    ))}
                  </AvatarGroup>
                  <div className="flex flex-row gap-1">
                    <Button color="success" onClick={() => acceptGroup(group)}>
                      Aceptar
                    </Button>
                    <Button color="danger" onClick={() => declineGroup(group)}>
                      Rechazar
                    </Button>
                  </div>
                </div>
              );
            })}
          </>
        )}
      </div>
      <GroupCreateModal groups={groups} setGroups={setGroups} />
      <button onClick={() => setOpen(!open)}>
        <div className="absolute top-1/2 rounded-full left-[-25px] w-[50px] h-[50px] bg-gray-800 flex items-center justify-center">
          <motion.div initial={{ rotate: 0, right: -10 }} animate={{ rotate: open ? 180 : 0, x: open ? 0 : -10 }} className="w-[30px] h-[30px]">
            <FaAngleLeft className="h-full w-full text-white" />
          </motion.div>
        </div>
      </button>
    </div>
  );
}
