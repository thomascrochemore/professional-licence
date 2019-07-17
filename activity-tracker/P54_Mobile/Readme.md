Structure refactorisée du mobile :
la structure a été faite sur MenuActivity, le reste reste à faire.
j'ai enlevé tous les onclick dans les vues car ils ne faisaient appel qu'aux fonction de l'activité,
et quand c'est un bouton qui est dans le fragment, dur de s'en sortir niveau code.
J'ai parcontre fait une petite classe mère qui permet d'alléger les lignes de code, 10 onClickLitener, ça fait lourd.

La première chose à faire c'est pour chaque activity, l'hériter de la classe BaseActivity
Ensuite quand on veut naviguer directement dans le code :
- la fonction navigate de la classe BaseActivity permet de naviguer entre différents fragments de l'activity
- la fonction pushActivity de la classe BaseActiv permet d'empiler une nouvelle activité
Quand on veut qu'un bouton soit un "lien" vers une nouvelle page :
- la fonction bindNavigate permet d'appeler la fonction navigate au click d'un bouton, elle doit être appelée dans onResume
- la fonction bindPushActivity permet d'appeler la fonction pushActivity au click d'un bouton, elle doit être appelée dans onResume
- la fonction unbind permet de détacher le onClick du bouton souhaité, elle doit être appelée dans onPause pour chaque bind fait dans onResume

Bien entendu on peut l'utiliser dans les fragments
- getActivity permet de récupérer l'activité
- il faut ensuite la caster dans ton activité pour pouvoir avoir accès à ces fonctions.

La partie service sera faite demain, petit problème, on ne peut pas faire du network dans le thread principal, cela demande un petit peu de gymnastique.