PGDMP                  
    |         
   ProjectPMS    16.2    16.2 +                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            !           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            "           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            #           1262    58890 
   ProjectPMS    DATABASE     �   CREATE DATABASE "ProjectPMS" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Vietnamese_Vietnam.1252';
    DROP DATABASE "ProjectPMS";
                postgres    false            �            1259    58892    comments    TABLE     �   CREATE TABLE public.comments (
    commentid integer NOT NULL,
    author character varying(255),
    create_date timestamp(6) without time zone,
    description character varying(255),
    task_id integer NOT NULL
);
    DROP TABLE public.comments;
       public         heap    postgres    false            �            1259    58891    comments_commentid_seq    SEQUENCE     �   ALTER TABLE public.comments ALTER COLUMN commentid ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.comments_commentid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    216            �            1259    58899    member_project    TABLE     e   CREATE TABLE public.member_project (
    project_id bigint NOT NULL,
    user_id integer NOT NULL
);
 "   DROP TABLE public.member_project;
       public         heap    postgres    false            �            1259    58904    member_task    TABLE     `   CREATE TABLE public.member_task (
    task_id integer NOT NULL,
    user_id integer NOT NULL
);
    DROP TABLE public.member_task;
       public         heap    postgres    false            �            1259    58910    projects    TABLE     �   CREATE TABLE public.projects (
    id bigint NOT NULL,
    description character varying(255),
    name character varying(255),
    manager_id integer
);
    DROP TABLE public.projects;
       public         heap    postgres    false            �            1259    58909    projects_id_seq    SEQUENCE     �   ALTER TABLE public.projects ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.projects_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    220            �            1259    58918 	   resources    TABLE     G  CREATE TABLE public.resources (
    resource_id integer NOT NULL,
    capacity integer,
    expiration_date date,
    licence_key character varying(255),
    name character varying(255),
    new_attribute character varying(255),
    total_money numeric(38,2),
    type character varying(255),
    project_id bigint NOT NULL
);
    DROP TABLE public.resources;
       public         heap    postgres    false            �            1259    58917    resources_resource_id_seq    SEQUENCE     �   ALTER TABLE public.resources ALTER COLUMN resource_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.resources_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    222            �            1259    58926    tasks    TABLE     �   CREATE TABLE public.tasks (
    taskid integer NOT NULL,
    due_date date,
    projectid integer,
    title character varying(255)
);
    DROP TABLE public.tasks;
       public         heap    postgres    false            �            1259    58925    tasks_taskid_seq    SEQUENCE     �   ALTER TABLE public.tasks ALTER COLUMN taskid ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tasks_taskid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    224            �            1259    58932    users    TABLE     �   CREATE TABLE public.users (
    userid integer NOT NULL,
    password character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false            �            1259    58931    users_userid_seq    SEQUENCE     �   ALTER TABLE public.users ALTER COLUMN userid ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.users_userid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    226                      0    58892    comments 
   TABLE DATA           X   COPY public.comments (commentid, author, create_date, description, task_id) FROM stdin;
    public          postgres    false    216   �2                 0    58899    member_project 
   TABLE DATA           =   COPY public.member_project (project_id, user_id) FROM stdin;
    public          postgres    false    217   3                 0    58904    member_task 
   TABLE DATA           7   COPY public.member_task (task_id, user_id) FROM stdin;
    public          postgres    false    218   83                 0    58910    projects 
   TABLE DATA           E   COPY public.projects (id, description, name, manager_id) FROM stdin;
    public          postgres    false    220   U3                 0    58918 	   resources 
   TABLE DATA           �   COPY public.resources (resource_id, capacity, expiration_date, licence_key, name, new_attribute, total_money, type, project_id) FROM stdin;
    public          postgres    false    222   r3                 0    58926    tasks 
   TABLE DATA           C   COPY public.tasks (taskid, due_date, projectid, title) FROM stdin;
    public          postgres    false    224   �3                 0    58932    users 
   TABLE DATA           ;   COPY public.users (userid, password, username) FROM stdin;
    public          postgres    false    226   �3       $           0    0    comments_commentid_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.comments_commentid_seq', 1, false);
          public          postgres    false    215            %           0    0    projects_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.projects_id_seq', 1, false);
          public          postgres    false    219            &           0    0    resources_resource_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.resources_resource_id_seq', 1, false);
          public          postgres    false    221            '           0    0    tasks_taskid_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.tasks_taskid_seq', 1, false);
          public          postgres    false    223            (           0    0    users_userid_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.users_userid_seq', 1, false);
          public          postgres    false    225            m           2606    58898    comments comments_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (commentid);
 @   ALTER TABLE ONLY public.comments DROP CONSTRAINT comments_pkey;
       public            postgres    false    216            o           2606    58903 "   member_project member_project_pkey 
   CONSTRAINT     q   ALTER TABLE ONLY public.member_project
    ADD CONSTRAINT member_project_pkey PRIMARY KEY (project_id, user_id);
 L   ALTER TABLE ONLY public.member_project DROP CONSTRAINT member_project_pkey;
       public            postgres    false    217    217            q           2606    58908    member_task member_task_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.member_task
    ADD CONSTRAINT member_task_pkey PRIMARY KEY (task_id, user_id);
 F   ALTER TABLE ONLY public.member_task DROP CONSTRAINT member_task_pkey;
       public            postgres    false    218    218            s           2606    58916    projects projects_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.projects DROP CONSTRAINT projects_pkey;
       public            postgres    false    220            u           2606    58924    resources resources_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY public.resources
    ADD CONSTRAINT resources_pkey PRIMARY KEY (resource_id);
 B   ALTER TABLE ONLY public.resources DROP CONSTRAINT resources_pkey;
       public            postgres    false    222            w           2606    58930    tasks tasks_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (taskid);
 :   ALTER TABLE ONLY public.tasks DROP CONSTRAINT tasks_pkey;
       public            postgres    false    224            y           2606    58940 !   users ukr43af9ap4edm43mmtq01oddj6 
   CONSTRAINT     `   ALTER TABLE ONLY public.users
    ADD CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username);
 K   ALTER TABLE ONLY public.users DROP CONSTRAINT ukr43af9ap4edm43mmtq01oddj6;
       public            postgres    false    226            {           2606    58938    users users_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (userid);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    226            �           2606    58971 %   resources fk4dekmr3028xq5q54nj338ywya    FK CONSTRAINT     �   ALTER TABLE ONLY public.resources
    ADD CONSTRAINT fk4dekmr3028xq5q54nj338ywya FOREIGN KEY (project_id) REFERENCES public.projects(id);
 O   ALTER TABLE ONLY public.resources DROP CONSTRAINT fk4dekmr3028xq5q54nj338ywya;
       public          postgres    false    4723    222    220            }           2606    58951 *   member_project fk8yb29sfpk9bfwqjvxrld459by    FK CONSTRAINT     �   ALTER TABLE ONLY public.member_project
    ADD CONSTRAINT fk8yb29sfpk9bfwqjvxrld459by FOREIGN KEY (project_id) REFERENCES public.projects(id);
 T   ALTER TABLE ONLY public.member_project DROP CONSTRAINT fk8yb29sfpk9bfwqjvxrld459by;
       public          postgres    false    220    217    4723                       2606    58956 '   member_task fkajktqrirllcxuh057g7jobr4b    FK CONSTRAINT     �   ALTER TABLE ONLY public.member_task
    ADD CONSTRAINT fkajktqrirllcxuh057g7jobr4b FOREIGN KEY (user_id) REFERENCES public.users(userid);
 Q   ALTER TABLE ONLY public.member_task DROP CONSTRAINT fkajktqrirllcxuh057g7jobr4b;
       public          postgres    false    4731    226    218            ~           2606    58946 *   member_project fkcvk4ro2vxnor2n9mlt1lb3ce5    FK CONSTRAINT     �   ALTER TABLE ONLY public.member_project
    ADD CONSTRAINT fkcvk4ro2vxnor2n9mlt1lb3ce5 FOREIGN KEY (user_id) REFERENCES public.users(userid);
 T   ALTER TABLE ONLY public.member_project DROP CONSTRAINT fkcvk4ro2vxnor2n9mlt1lb3ce5;
       public          postgres    false    4731    217    226            �           2606    58961 '   member_task fkhf6hjrgu93a0h8qc3cms0xdf8    FK CONSTRAINT     �   ALTER TABLE ONLY public.member_task
    ADD CONSTRAINT fkhf6hjrgu93a0h8qc3cms0xdf8 FOREIGN KEY (task_id) REFERENCES public.tasks(taskid);
 Q   ALTER TABLE ONLY public.member_task DROP CONSTRAINT fkhf6hjrgu93a0h8qc3cms0xdf8;
       public          postgres    false    218    4727    224            |           2606    58941 $   comments fki7pp0331nbiwd2844kg78kfwb    FK CONSTRAINT     �   ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fki7pp0331nbiwd2844kg78kfwb FOREIGN KEY (task_id) REFERENCES public.tasks(taskid);
 N   ALTER TABLE ONLY public.comments DROP CONSTRAINT fki7pp0331nbiwd2844kg78kfwb;
       public          postgres    false    4727    216    224            �           2606    58966 #   projects fkurl8wb4qjly2c5xwdcpetuxs    FK CONSTRAINT     �   ALTER TABLE ONLY public.projects
    ADD CONSTRAINT fkurl8wb4qjly2c5xwdcpetuxs FOREIGN KEY (manager_id) REFERENCES public.users(userid);
 M   ALTER TABLE ONLY public.projects DROP CONSTRAINT fkurl8wb4qjly2c5xwdcpetuxs;
       public          postgres    false    220    4731    226                  x������ � �            x������ � �            x������ � �            x������ � �            x������ � �            x������ � �            x�3�4426�t)Mv�������� (��     